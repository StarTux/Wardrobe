package com.cavetale.wardrobe;

import com.cavetale.core.font.GuiOverlay;
import com.cavetale.mytems.Mytems;
import com.cavetale.wardrobe.sql.SQLPackage;
import com.cavetale.wardrobe.util.Gui;
import com.cavetale.wardrobe.util.Items;
import com.winthier.playercache.PlayerCache;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.event.ClickEvent.openUrl;
import static net.kyori.adventure.text.event.HoverEvent.showText;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextColor.color;
import static net.kyori.adventure.text.format.TextDecoration.*;

@RequiredArgsConstructor
public final class WardrobeCommand implements TabExecutor {
    private final WardrobePlugin plugin;
    public static final TextColor BG = color(0x8080D0);
    public static final TextColor COLOR = color(0xD08080);
    private final List<MenuButton> menuButtonList = new ArrayList<>();

    @Value
    private static final class MenuButton {
        protected final Category category;
    }

    public void enable() {
        menuButtonList.clear();
        for (Category category : Category.values()) {
            menuButtonList.add(new MenuButton(category));
        }
        plugin.getCommand("wardrobe").setExecutor(this);
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String alias, final String[] args) {
        if (args.length == 3 && sender.hasPermission("wardrobe.admin") && args[0].equals("unlock")) {
            UUID uuid = PlayerCache.uuidForName(args[1]);
            if (uuid == null) {
                sender.sendMessage("Player not found: " + args[1]);
                return true;
            }
            Package pack = Package.of(args[2]);
            if (pack == null) {
                sender.sendMessage("Package not found: " + args[2]);
                return true;
            }
            SQLPackage row = new SQLPackage(uuid, pack);
            if (0 == plugin.database.insertIgnore(row)) {
                sender.sendMessage("" + args[1] + " already owns package " + pack + "!");
            } else {
                sender.sendMessage(pack + " unlocked for " + args[1]);
            }
            Player target = Bukkit.getPlayer(uuid);
            if (target != null) {
                target.showTitle(Title.title(pack.displayName,
                                             text("Wardrobe Unlocked!", GREEN)));
                target.playSound(target.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE,
                                 SoundCategory.MASTER, 0.5f, 1.5f);
            }
            return true;
        }
        if (args.length == 1 && sender instanceof Player player && args[0].equals("click")) {
            click(player);
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("[wardrobe:wardrobe] player expected");
            return true;
        }
        openGui((Player) sender);
        return true;
    }

    protected void openGui(Player player) {
        GuiContext context = new GuiContext();
        if (player.hasPermission("wardrobe.all")) {
            context.unlockedPackages.addAll(List.of(Package.values()));
            context.unlockedItems.addAll(WardrobeItem.all());
            openGui(player, context);
        } else {
            plugin.database.find(SQLPackage.class)
                .eq("player", player.getUniqueId())
                .findListAsync(list -> {
                        for (SQLPackage row : list) {
                            Package pack = row.getPackage();
                            if (pack == null) continue;
                            context.unlockedPackages.add(pack);
                            context.unlockedItems.addAll(pack.wardrobeItems);
                        }
                        for (FlagWardrobeItem it : FlagWardrobeItem.values()) {
                            if (it.has(player)) context.unlockedItems.add(it);
                        }
                        openGui(player, context);
                    });
        }
    }

    protected void openGui(Player player, GuiContext context) {
        final int size = 6 * 9;
        GuiOverlay.Builder builder = GuiOverlay.BLANK.builder(size, COLOR); // Category
        Gui gui = new Gui(plugin).size(size);
        int topBarIndex = 1;
        for (MenuButton menuButton : menuButtonList) {
            if (menuButton.category != null) {
                gui.setItem(menuButton.category.guiIndex,
                            Items.text(menuButton.category.createIcon(),
                                       List.of(menuButton.category.displayName,
                                               text("Category", DARK_GRAY))),
                            (p, click) -> {
                                if (click.getClick() != ClickType.LEFT) return;
                                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK,
                                                 SoundCategory.MASTER, 1.0f, 1.0f);
                                context.selectedCategory = menuButton.category;
                                context.page = 0;
                                openGui(p, context);
                            });
                if (context.selectedCategory == menuButton.category) {
                    builder.tab(menuButton.category.guiIndex, COLOR, BG);
                }
            }
        }
        final List<WardrobeItem> itemList = switch (context.selectedCategory) {
        case ALL -> WardrobeItem.all();
        //case UNLOCKED -> new ArrayList<>(context.unlockedItems);
        default -> context.selectedCategory.getItems();
        };
        Collections.sort(itemList, (a, b) -> {
                boolean aa = context.unlockedItems.contains(a);
                boolean bb = context.unlockedItems.contains(b);
                if (aa && !bb) return -1;
                if (bb && !aa) return 1;
                return Integer.compare(a.getCategory().ordinal() * 1000 + a.ordinal(),
                                       b.getCategory().ordinal() * 1000 + b.ordinal());
            });
        final int pageSize = 5 * 9;
        final int pageCount = (itemList.size() - 1) / pageSize + 1;
        builder.title(textOfChildren(context.selectedCategory.displayName,
                                     text(" " + (context.page + 1) + "/" + pageCount)));
        final int listOffset = context.page * pageSize;
        for (int i = 0; i < pageSize; i += 1) {
            final int guiIndex = i + 9;
            final int listIndex = listOffset + i;
            if (listIndex >= itemList.size()) break;
            final WardrobeItem wardrobeItem = itemList.get(listIndex);
            final boolean unlocked = context.unlockedItems.contains(wardrobeItem);
            final ItemStack icon;
            if (unlocked) {
                icon = Items.text(wardrobeItem.toMenuItem(), wardrobeItem.getMenuTooltip());
            } else {
                icon = Items.text(wardrobeItem.toMenuItem(),
                                  List.of(wardrobeItem.getDisplayName(),
                                          text("Wardrobe Item", DARK_GRAY),
                                          textOfChildren(Mytems.CROSSED_CHECKBOX, text(" Locked", RED)),
                                          empty(),
                                          text("Purchase this item at ", GRAY),
                                          textOfChildren(text("at ", GRAY), text("store.cavetale.com", BLUE, UNDERLINED))));
            }
            gui.setItem(guiIndex, icon, (p, click) -> {
                    if (!click.isLeftClick() && !click.isRightClick()) return;
                    if (unlocked) {
                        wardrobeItem.onClick(p, click);
                        openGui(p, context);
                    } else {
                        onClickUnowned(p, wardrobeItem);
                    }
                });
            if (wardrobeItem.isWearing(player)) {
                builder.highlightSlot(guiIndex, GOLD);
            } else if (unlocked) {
                builder.highlightSlot(guiIndex, COLOR);
            }
        }
        if (context.page > 0) {
            gui.setItem(0, Mytems.ARROW_LEFT.createIcon(List.of(text("Page " + context.page, GRAY))), (p, click) -> {
                    if (!click.isLeftClick()) return;
                    player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, SoundCategory.MASTER, 1.0f, 1.0f);
                    context.page -= 1;
                    openGui(p, context);
                });
        }
        if (context.page < pageCount - 1) {
            gui.setItem(8, Mytems.ARROW_RIGHT.createIcon(List.of(text("Page " + (context.page + 2), GRAY))), (p, click) -> {
                    if (!click.isLeftClick()) return;
                    player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, SoundCategory.MASTER, 1.0f, 1.0f);
                    context.page += 1;
                    openGui(p, context);
                });
        }
        gui.title(builder.build());
        gui.open(player);
    }

    private void onClickUnowned(Player player, WardrobeItem wardrobeItem) {
        Component url = wardrobeItem.getCategory() == Category.MOUNT
            ? text("store.cavetale.com/category/mounts", BLUE, UNDERLINED)
            : text("store.cavetale.com/category/wardrobe", BLUE, UNDERLINED);
        player.sendMessage(textOfChildren(newline(), text("Purchase this item at ", GRAY), Mytems.MOUSE_LEFT, url, newline())
                           .clickEvent(openUrl("https://store.cavetale.com/category/wardrobe"))
                           .hoverEvent(showText(url)));
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        return List.of();
    }

    protected static final class GuiContext {
        Category selectedCategory = Category.ALL;
        int page;
        Set<Package> unlockedPackages = EnumSet.noneOf(Package.class);
        Set<WardrobeItem> unlockedItems = new HashSet<>();
    }

    private void click(Player player) {
        final int size = 5 * 9;
        GuiOverlay.Builder builder = GuiOverlay.BLANK.builder(size, color(0xFFA3C3))
            .layer(GuiOverlay.TITLE_BAR, BG)
            .title(text("This heart beats for you", WHITE));
        Gui gui = new Gui(plugin).size(size);
        gui.setItem(4, 1, Mytems.HEART.createIcon(List.of(text("this heart beats for you", color(0xFFA3C3)))), (p, click) -> {
                p.sendMessage(textOfChildren(newline(),
                                             Mytems.MOUSE_LEFT,
                                             text(" store.cavetale.com/category/shine", color(0xFFA3C3)),
                                             newline())
                              .clickEvent(openUrl("https://store.cavetale.com/category/valentine"))
                              .hoverEvent(showText(text("store.cavetale.com/category/valentine", color(0xFFA3C3), UNDERLINED))));
            });
        gui.setItem(4, 3, Mytems.CUPID_WINGS.createIcon(List.of(text("these wings fly for you", color(0xFFA3C3)))), (p, click) -> {
                p.sendMessage(textOfChildren(newline(),
                                             Mytems.MOUSE_LEFT,
                                             text(" store.cavetale.com/category/valentine", color(0xFFA3C3)),
                                             newline())
                              .clickEvent(openUrl("https://store.cavetale.com/category/valentine"))
                              .hoverEvent(showText(text("store.cavetale.com/category/valentine", color(0xFFA3C3), UNDERLINED))));
            });
        gui.title(builder.build());
        gui.open(player);
    }
}
