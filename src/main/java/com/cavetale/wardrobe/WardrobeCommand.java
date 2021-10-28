package com.cavetale.wardrobe;

import com.cavetale.core.font.GuiOverlay;
import com.cavetale.wardrobe.sql.SQLPackage;
import com.cavetale.wardrobe.util.Gui;
import com.cavetale.wardrobe.util.Items;
import com.winthier.playercache.PlayerCache;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
        plugin.database.find(SQLPackage.class)
            .eq("player", player.getUniqueId())
            .findListAsync(list -> openGui(player, list));
    }

    protected void openGui(Player player, List<SQLPackage> packages) {
        Set<WardrobeItem> unlocked = new HashSet<>();
        for (SQLPackage row : packages) {
            Package pack = row.getPackage();
            unlocked.addAll(pack.wardrobeItems);
        }
        int size = 3 * 9;
        Component title = GuiOverlay.builder(size)
            .layer(GuiOverlay.BLANK, COLOR)
            .layer(GuiOverlay.TOP_BAR, BG)
            .title(Component.text("Wardrobe", COLOR))
            .build();
        Gui gui = new Gui(plugin).title(title).size(size);
        int topBarIndex = 2;
        for (MenuButton menuButton : menuButtonList) {
            if (menuButton.category != null) {
                Category category = menuButton.category;
                gui.setItem(topBarIndex++,
                            Items.text(category.wardrobeItems.get(0).toMenuItem(),
                                       List.of(category.displayName,
                                               Component.text("Category", NamedTextColor.DARK_GRAY))),
                            (p, click) -> onClickCategory(p, click, category, unlocked));
            }
        }
        fillCategory(player, gui, Category.WHITE_BUNNY, unlocked);
        gui.open(player);
    }

    protected void fillCategory(Player player, Gui gui, Category category, Set<WardrobeItem> unlocked) {
        for (int i = 0; i < gui.getSize() - 9; i += 1) {
            int index = i + 9;
            if (i >= category.wardrobeItems.size()) {
                gui.setItem(index, null);
                continue;
            }
            WardrobeItem wardrobeItem = category.wardrobeItems.get(i);
            if (unlocked.contains(wardrobeItem)) {
                gui.setItem(index, wardrobeItem.toMenuItem(), wardrobeItem::onClick);
            } else {
                gui.setItem(index, unowned(wardrobeItem.toMenuItem()), this::onClickUnowned);
            }
        }
    }

    protected ItemStack unowned(ItemStack in) {
        ItemMeta meta = in.getItemMeta();
        meta.lore(Arrays.asList(Component.text("Purchase this item at ").color(COLOR)
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

    protected void onClickCategory(Player player, InventoryClickEvent event, Category category, Set<WardrobeItem> unlocked) {
        if (event.getClick() != ClickType.LEFT) return;
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1.0f, 1.0f);
        Gui gui = Gui.of(player);
        if (gui == null) return;
        fillCategory(player, gui, category, unlocked);
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        return Collections.emptyList();
    }
}
