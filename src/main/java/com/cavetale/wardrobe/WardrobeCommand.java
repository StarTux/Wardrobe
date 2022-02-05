package com.cavetale.wardrobe;

import com.cavetale.core.font.GuiOverlay;
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
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@RequiredArgsConstructor
public final class WardrobeCommand implements TabExecutor {
    private final WardrobePlugin plugin;
    public static final TextColor BG = TextColor.color(0x8080D0);
    public static final TextColor COLOR = TextColor.color(0xD08080);
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
                                             Component.text("Wardrobe Unlocked!", NamedTextColor.GREEN)));
                target.playSound(target.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE,
                                 SoundCategory.MASTER, 0.5f, 1.5f);
            }
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
                        openGui(player, context);
                    });
        }
    }

    protected void openGui(Player player, GuiContext context) {
        final int size = 3 * 9;
        Component title = context.selectedPackage != null
            ? (GuiOverlay.builder(size) // Package
               .layer(GuiOverlay.BLANK, TextColor.color(0x302010))
               .layer(GuiOverlay.TOP_BAR, TextColor.color(0x404050))
               .title(Component.join(JoinConfiguration.noSeparators(),
                                     context.selectedPackage.displayName,
                                     Component.text(" - Package", COLOR)))
               .build())
            : (GuiOverlay.builder(size) // Category
               .layer(GuiOverlay.BLANK, COLOR)
               .layer(GuiOverlay.TOP_BAR, BG)
               .title(Component.join(JoinConfiguration.noSeparators(),
                                     context.selectedCategory.displayName,
                                     Component.text(" - Wardrobe", COLOR)))
               .build());
        Gui gui = new Gui(plugin).title(title).size(size);
        context.gui = gui;
        int topBarIndex = 1;
        for (MenuButton menuButton : menuButtonList) {
            if (menuButton.category != null) {
                gui.setItem(topBarIndex++,
                            Items.text(menuButton.category.icon,
                                       List.of(menuButton.category.displayName,
                                               Component.text("Category", NamedTextColor.DARK_GRAY))),
                            (p, click) -> {
                                if (click.getClick() != ClickType.LEFT) return;
                                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK,
                                                 SoundCategory.MASTER, 1.0f, 1.0f);
                                context.selectedCategory = menuButton.category;
                                context.selectedPackage = null;
                                openGui(p, context);
                            });
            }
        }
        if (context.selectedPackage != null) {
            fillPackage(player, context);
        } else {
            fillCategory(player, context);
        }
        gui.open(player);
    }

    protected void fillCategory(Player player, GuiContext context) {
        List<Package> packageList = context.selectedCategory == Category.UNLOCKED
            ? (context.selectedCategory.packages.stream()
               .filter(context.unlockedPackages::contains)
               .collect(Collectors.toList()))
            : context.selectedCategory.packages;
        for (int i = 0; i < context.gui.getSize() - 9; i += 1) {
            int index = i + 9;
            if (i >= packageList.size()) {
                context.gui.setItem(index, null);
                continue;
            }
            Package pack = packageList.get(i);
            WardrobeItem firstItem = pack.wardrobeItems.get(0);
            context.gui.setItem(index,
                                Items.text(firstItem.toMenuItem(),
                                           List.of(pack.displayName,
                                                   Component.text("Package", NamedTextColor.DARK_GRAY),
                                                   (context.unlockedPackages.contains(pack)
                                                    ? Component.text("Unlocked", NamedTextColor.GREEN)
                                                    : Component.text("Locked", NamedTextColor.RED)))),
                                (p, click) -> {
                                    if (click.getClick() != ClickType.LEFT) return;
                                    p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK,
                                                SoundCategory.MASTER, 1.0f, 1.0f);
                                    context.selectedPackage = pack;
                                    openGui(p, context);
                                });
        }
    }

    protected void fillPackage(Player player, GuiContext context) {
        List<WardrobeItem> itemList = context.selectedPackage.wardrobeItems;
        for (int i = 0; i < context.gui.getSize() - 9; i += 1) {
            int index = i + 9;
            if (i >= itemList.size()) {
                context.gui.setItem(index, null);
                continue;
            }
            WardrobeItem wardrobeItem = itemList.get(i);
            if (context.unlockedItems.contains(wardrobeItem)) {
                context.gui.setItem(index, wardrobeItem.toMenuItem(), (p, e) -> {
                        wardrobeItem.onClick(p, e);
                        if (wardrobeItem instanceof Costume) {
                            openGui(p, context);
                        }
                    });
            } else {
                context.gui.setItem(index, unowned(wardrobeItem.toMenuItem()), this::onClickUnowned);
            }
        }
        context.gui.setItem(Gui.OUTSIDE, null, (p, click) -> {
                player.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK,
                                 SoundCategory.MASTER, 1.0f, 1.0f);
                context.selectedPackage = null;
                openGui(p, context);
            });
    }

    protected ItemStack unowned(ItemStack in) {
        ItemMeta meta = in.getItemMeta();
        meta.lore(List.of(Component.text("Purchase this item at ").color(COLOR)
                          .decoration(TextDecoration.ITALIC, false),
                          Component.text("store.cavetale.com").color(COLOR)
                          .decorate(TextDecoration.UNDERLINED).decoration(TextDecoration.ITALIC, false)));
        in.setItemMeta(meta);
        return in;
    }

    protected void onClickUnowned(Player player, InventoryClickEvent event) {
        if (event.getClick() != ClickType.LEFT) return;
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1.0f, 1.0f);
        Component url = Component.text("store.cavetale.com/wardrobe", COLOR, TextDecoration.UNDERLINED);
        player.sendMessage(Component.text()
                           .append(Component.newline())
                           .append(Component.text("Purchase this item at ", COLOR))
                           .append(url)
                           .clickEvent(ClickEvent.openUrl("https://store.cavetale.com/category/wardrobe"))
                           .hoverEvent(HoverEvent.showText(url))
                           .append(Component.newline())
                           .build());
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        return Collections.emptyList();
    }

    protected static final class GuiContext {
        Gui gui;
        Category selectedCategory = Category.ALL;
        Package selectedPackage;
        Set<Package> unlockedPackages = EnumSet.noneOf(Package.class);
        Set<WardrobeItem> unlockedItems = new HashSet<>();
    }
}
