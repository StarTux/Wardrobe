package com.cavetale.wardrobe;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Abstraction for an ItemStack which is belongs in the wardrobe.
 */
public interface WardrobeItem {
    static WardrobeItem of(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) return null;
        Hat hat = Hat.of(itemStack);
        if (hat != null) return hat;
        Handheld handheld = Handheld.of(itemStack);
        if (handheld != null) return handheld;
        return null;
    }

    Component getDisplayName();
}
