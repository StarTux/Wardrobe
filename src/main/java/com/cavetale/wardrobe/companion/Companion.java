package com.cavetale.wardrobe.companion;

import org.bukkit.entity.Player;

public interface Companion {
    CompanionType getType();

    void start(Player player);

    void tick();

    void stop();
}
