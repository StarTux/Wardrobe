package com.cavetale.wardrobe;

import com.cavetale.wardrobe.sql.SQLPackage;
import com.cavetale.wardrobe.util.Gui;
import com.winthier.sql.SQLDatabase;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class WardrobePlugin extends JavaPlugin {
    @Getter protected static WardrobePlugin instance;
    protected WardrobeCommand wardrobeCommand = new WardrobeCommand(this);
    protected EventListener eventListener = new EventListener(this);
    protected SQLDatabase database = new SQLDatabase(this);

    @Override
    public void onEnable() {
        instance = this;
        database.registerTables(SQLPackage.class);
        if (!database.createAllTables()) {
            getLogger().warning("Database creation failed! Plugin disabled.");
            setEnabled(false);
            return;
        }
        wardrobeCommand.enable();
        eventListener.enable();
        Gui.enable(this);
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            removeAll(player);
        }
        Gui.disable(this);
    }

    void removeAll(Player player) {
        Costume.remove(player);
        Hat.remove(player);
        Handheld.remove(player, false);
        Handheld.remove(player, true);
    }
}
