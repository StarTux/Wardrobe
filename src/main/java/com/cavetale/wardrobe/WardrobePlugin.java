package com.cavetale.wardrobe;

import com.cavetale.wardrobe.emote.Emotes;
import com.cavetale.wardrobe.mount.Ride;
import com.cavetale.wardrobe.session.Session;
import com.cavetale.wardrobe.session.Sessions;
import com.cavetale.wardrobe.sql.SQLEmote;
import com.cavetale.wardrobe.sql.SQLEquipped;
import com.cavetale.wardrobe.sql.SQLPackage;
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
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        database.registerTables(List.of(SQLPackage.class, SQLEmote.class, SQLEquipped.class));
        if (!database.createAllTables()) {
            getLogger().warning("Database creation failed! Plugin disabled.");
            setEnabled(false);
            return;
        }
        wardrobeCommand.enable();
        adminCommand.enable();
        eventListener.enable();
        sessions.enable();
        new MenuListener(this).enable();
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            removeAll(player);
        }
        Ride.cancelAll();
    }

    protected void removeAll(Player player) {
        Costume.remove(player);
        Hat.remove(player);
        Handheld.remove(player, false);
        Handheld.remove(player, true);
        ShoulderEntity.remove(player, false);
        ShoulderEntity.remove(player, true);
    }

    public static WardrobePlugin plugin() {
        return instance;
    }

    public static SQLDatabase database() {
        return instance.database;
    }

    public static Sessions sessions() {
        return instance.sessions;
    }

    public static Session sessionOf(Player player) {
        return instance.sessions.of(player);
    }
}
