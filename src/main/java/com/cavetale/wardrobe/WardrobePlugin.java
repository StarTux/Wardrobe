package com.cavetale.wardrobe;

import com.cavetale.wardrobe.emote.Emotes;
import com.cavetale.wardrobe.mount.Ride;
import com.cavetale.wardrobe.session.Session;
import com.cavetale.wardrobe.session.Sessions;
import com.cavetale.wardrobe.sql.SQLEmote;
import com.cavetale.wardrobe.sql.SQLPackage;
import com.cavetale.wardrobe.util.Gui;
import com.winthier.sql.SQLDatabase;
import java.util.List;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class WardrobePlugin extends JavaPlugin {
    @Getter protected static WardrobePlugin instance;
    protected WardrobeCommand wardrobeCommand = new WardrobeCommand(this);
    protected AdminCommand adminCommand = new AdminCommand(this);
    protected EventListener eventListener = new EventListener(this);
    protected SQLDatabase database = new SQLDatabase(this);
    @Getter protected Emotes emotes = new Emotes();
    @Getter protected Sessions sessions = new Sessions(this);

    @Override
    public void onEnable() {
        instance = this;
        database.registerTables(List.of(SQLPackage.class, SQLEmote.class));
        if (!database.createAllTables()) {
            getLogger().warning("Database creation failed! Plugin disabled.");
            setEnabled(false);
            return;
        }
        wardrobeCommand.enable();
        adminCommand.enable();
        eventListener.enable();
        Gui.enable(this);
        sessions.enable();
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            removeAll(player);
        }
        Gui.disable(this);
        Ride.cancelAll();
    }

    protected void removeAll(Player player) {
        Costume.remove(player);
        Hat.remove(player);
        Handheld.remove(player, false);
        Handheld.remove(player, true);
    }

    public static WardrobePlugin plugin() {
        return instance;
    }

    public static Sessions sessions() {
        return instance.sessions;
    }

    public static Session sessionOf(Player player) {
        return instance.sessions.of(player);
    }
}
