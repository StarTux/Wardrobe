package com.cavetale.wardrobe;

import com.cavetale.core.command.AbstractCommand;
import com.cavetale.core.command.CommandArgCompleter;
import com.cavetale.core.command.CommandWarn;
import com.cavetale.wardrobe.sql.SQLPackage;
import com.winthier.playercache.PlayerCache;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import static net.kyori.adventure.text.Component.join;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.JoinConfiguration.noSeparators;
import static net.kyori.adventure.text.JoinConfiguration.separator;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.title.Title.title;

public final class AdminCommand extends AbstractCommand<WardrobePlugin> {
    protected AdminCommand(final WardrobePlugin plugin) {
        super(plugin, "wardrobeadmin");
    }

    @Override
    protected void onEnable() {
        rootNode.addChild("player").arguments("<player>")
            .description("List player packages")
            .completers(PlayerCache.NAME_COMPLETER)
            .senderCaller(this::player);
        rootNode.addChild("unlock").arguments("<player> <package")
            .description("Unlock a package")
            .completers(PlayerCache.NAME_COMPLETER,
                        CommandArgCompleter.enumLowerList(Package.class))
            .senderCaller(this::unlock);
        rootNode.addChild("transfer").arguments("<from> <to>")
            .description("Account transfer")
            .completers(PlayerCache.NAME_COMPLETER, PlayerCache.NAME_COMPLETER)
            .senderCaller(this::transfer);
    }

    private boolean player(CommandSender sender, String[] args) {
        if (args.length != 1) return false;
        PlayerCache player = PlayerCache.forName(args[0]);
        if (player == null) throw new CommandWarn("Player not found: " + args[0]);
        List<SQLPackage> rows = plugin.database.find(SQLPackage.class)
            .eq("player", player.uuid)
            .findList();
        if (rows.isEmpty()) throw new CommandWarn(player.name + " does not have any packages");
        List<Component> components = new ArrayList<>();
        for (SQLPackage row : rows) {
            components.add(row.getPackage().displayName);
        }
        sender.sendMessage(join(noSeparators(),
                                text(player.name + " has " + rows.size() + " packages: ", AQUA),
                                join(separator(text(", ", GRAY)), components)));
        return true;
    }

    private boolean unlock(CommandSender sender, String[] args) {
        if (args.length != 2) return false;
        PlayerCache player = PlayerCache.forName(args[0]);
        if (player == null) throw new CommandWarn("Player not found: " + args[0]);
        Package pack = Package.of(args[1]);
        if (pack == null) throw new CommandWarn("Package not found: " + args[1]);
        SQLPackage row = new SQLPackage(player.uuid, pack);
        if (0 == plugin.database.insertIgnore(row)) {
            sender.sendMessage("" + args[0] + " already owns package " + pack + "!");
        } else {
            sender.sendMessage(pack + " unlocked for " + args[0]);
        }
        Player target = Bukkit.getPlayer(player.uuid);
        if (target != null) {
            target.showTitle(title(pack.displayName,
                                   text("Wardrobe Unlocked!", GREEN)));
            target.playSound(target.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE,
                             SoundCategory.MASTER, 0.5f, 1.5f);
        }
        return true;
    }

    private boolean transfer(CommandSender sender, String[] args) {
        if (args.length != 2) return false;
        PlayerCache from = PlayerCache.forArg(args[0]);
        if (from == null) throw new CommandWarn("Player not found: " + args[0]);
        PlayerCache to = PlayerCache.forArg(args[1]);
        if (to == null) throw new CommandWarn("Player not found: " + args[1]);
        if (from.equals(to)) throw new CommandWarn("Players are identical: " + from.getName());
        List<SQLPackage> rows = plugin.database.find(SQLPackage.class)
            .eq("player", from.uuid)
            .findList();
        if (rows.isEmpty()) throw new CommandWarn(from.name + " does not have any packages");
        int count = 0;
        for (SQLPackage row : rows) {
            SQLPackage newRow = new SQLPackage(to.uuid, row.getPackage());
            if (plugin.database.insertIgnore(newRow) != 0) count += 1;
        }
        plugin.database.delete(rows);
        sender.sendMessage(text("Packages transferred from " + from.name + " to " + to.name + ":"
                                + " rows=" + rows.size()
                                + " count=" + count));
        return true;
    }
}
