package com.cavetale.wardrobe;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Abstraction for Hat, Handheld, and Costume.  The first two are
 * actual items.
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

    ItemStack toMenuItem();

    void onClick(Player player, InventoryClickEvent event);
}
