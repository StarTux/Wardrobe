package com.cavetale.wardrobe;

import com.cavetale.wardrobe.sql.SQLPackage;
import com.cavetale.wardrobe.util.Gui;
import com.winthier.playercache.PlayerCache;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@RequiredArgsConstructor
public final class WardrobeCommand implements TabExecutor {
    private final WardrobePlugin plugin;
    public static final int COLOR = 0x4169E1;

    public void enable() {
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

    void openGui(Player player) {
        plugin.database.find(SQLPackage.class)
            .eq("player", player.getUniqueId())
            .findListAsync(list -> openGui(player, list));
    }

    void openGui(Player player, List<SQLPackage> packages) {
        Set<Enum> unlocked = new HashSet<>();
        for (SQLPackage row : packages) {
            Package pack = row.getPackage();
            if (pack == null) continue;
            if (pack.hat != null) unlocked.add(pack.hat);
            if (pack.costume != null) unlocked.add(pack.costume);
        }
        Gui gui = new Gui(plugin).title(Component.text("Wardrobe").color(TextColor.color(COLOR)).decorate(TextDecoration.BOLD));
        gui.size(9);
        int itemIndex = 0;
        for (Hat hat : Hat.values()) {
            int slot = itemIndex++;
            if (unlocked.contains(hat)) {
                gui.setItem(slot, hat.toItemStack(), click -> {
                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1.0f, 1.0f);
                        Hat removed = hat.remove(player);
                        if (removed == hat) {
                            player.sendMessage(Component.text("Hat removed: ").color(TextColor.color(COLOR))
                                               .append(removed.displayName));
                            return;
                        }
                        if (hat.wear(player)) {
                            player.sendMessage(Component.text("Hat equipped: ").color(TextColor.color(COLOR))
                                               .append(hat.displayName));
                        } else {
                            player.sendMessage(Component.text("Cannot equip ").color(TextColor.color(0xFF0000))
                                               .append(hat.displayName)
                                               .append(Component.text(": Inventory is full!").color(TextColor.color(0xFF0000))));
                        }
                    });
            } else {
                gui.setItem(slot, unowned(hat.toItemStack()));
            }
        }
        for (Costume costume : Costume.values()) {
            int slot = itemIndex++;
            if (unlocked.contains(costume)) {
                gui.setItem(slot, costume.toPlayerHead(), click -> {
                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1.0f, 1.0f);
                        if (costume == Costume.remove(player)) {
                            player.sendMessage(Component.text("Costume removed!").color(TextColor.color(COLOR)));
                            return;
                        }
                        costume.wear(player);
                        player.sendMessage(Component.text("Costume equipped: ").color(TextColor.color(COLOR))
                                           .append(costume.displayName));
                    });
            } else {
                gui.setItem(slot, unowned(costume.toPlayerHead()));
            }
        }
        gui.open(player);
    }

    ItemStack unowned(ItemStack in) {
        ItemMeta meta = in.getItemMeta();
        meta.lore(Arrays.asList(Component.text("Purchase this item at ").color(TextColor.color(COLOR))
                                .decoration(TextDecoration.ITALIC, false),
                                Component.text("store.cavetale.com").color(TextColor.color(COLOR))
                                .decorate(TextDecoration.UNDERLINED).decoration(TextDecoration.ITALIC, false)));
        in.setItemMeta(meta);
        return in;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        return Collections.emptyList();
    }
}
