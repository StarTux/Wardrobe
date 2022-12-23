package com.cavetale.wardrobe.mount;

import com.cavetale.wardrobe.Mount;
import org.bukkit.entity.Player;

public interface MountAdapter {
    MountResult mount(Player player, Mount mount);

    boolean unmount(Player player, Mount mount);
}
