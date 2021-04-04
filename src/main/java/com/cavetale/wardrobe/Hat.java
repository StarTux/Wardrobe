package com.cavetale.wardrobe;

import com.cavetale.mytems.Mytems;
import java.util.Arrays;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum Hat {
    WHITE_BUNNY_EARS(Component.text("White Bunny Ears").color(TextColor.color(0xFFFFFF)).decoration(TextDecoration.ITALIC, false),
                     Mytems.WHITE_BUNNY_EARS);

    public final Component displayName;
    public final Mytems mytems;

    Hat(final Component displayName, final Mytems mytems) {
        this.displayName = displayName;
        this.mytems = mytems;
    }

    /**
     * Attempt to empty the player helmet slot and put this hat
     * instead. It is recommended to call remove(Player) first.
     * @true if successful, false otherwise.
     */
    public boolean wear(Player player) {
        ItemStack itemStack = player.getInventory().getHelmet();
        if (itemStack != null && itemStack.getAmount() != 0) {
            player.getInventory().setHelmet(null);
            if (!player.getInventory().addItem(itemStack).isEmpty()) {
                // Can't add helmet item. Inventory must be full.
                player.getInventory().setHelmet(itemStack);
                return false;
            }
            itemStack = player.getInventory().getHelmet();
            if (itemStack != null && itemStack.getAmount() != 0) {
                // Item was added in helmet slot? Too bad.
                WardrobePlugin.getInstance().getLogger().warning("Item was added in helmet slot?");
                return false;
            }
        }
        // Helmet slot is empty now!
        itemStack = mytems.getMytem().getItem();
        player.getInventory().setHelmet(itemStack);
        return true;
    }

    /**
     * Remove any hat from the player.
     * @return the Hat that was removed or false
     */
    public static Hat remove(Player player) {
        Hat hat = of(player);
        if (hat == null) return null;
        player.getInventory().setHelmet(null);
        return hat;
    }

    /**
     * Get the hat that the player is currently wearing.
     * @return the worn Hat or null.
     */
    public static Hat of(Player player) {
        ItemStack itemStack = player.getInventory().getHelmet();
        if (itemStack == null || itemStack.getAmount() == 0) return null;
        Mytems mytems = Mytems.forItem(itemStack);
        if (mytems == null) return null;
        for (Hat hat : Hat.values()) {
            if (hat.mytems == mytems) return hat;
        }
        return null;
    }

    public static Hat of(ItemStack itemStack) {
        if (itemStack == null || itemStack.getAmount() == 0) return null;
        Mytems mytems = Mytems.forItem(itemStack);
        if (mytems == null) return null;
        for (Hat hat : Hat.values()) {
            if (hat.mytems == mytems) return hat;
        }
        return null;
    }

    public ItemStack toItemStack() {
        ItemStack itemStack = mytems.getMytem().getItem();
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(displayName);
        meta.lore(Arrays.asList(Component.text("Click to equip").color(TextColor.color(0xFFFF00)).decoration(TextDecoration.ITALIC, false)));
        meta.addItemFlags(ItemFlag.values());
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
