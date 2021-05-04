package com.cavetale.wardrobe;

import com.cavetale.mytems.Mytems;
import java.util.Arrays;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
public enum Handheld implements WardrobeItem {
    RED_LIGHTSABER(Component.text("Red Laser Sword", TextColor.color(0xff1d0b)), Mytems.RED_LIGHTSABER),
    BLUE_LIGHTSABER(Component.text("Blue Laser Sword", TextColor.color(0xadf3f3)), Mytems.BLUE_LIGHTSABER);

    public final Component displayName;
    public final Mytems mytems;

    Handheld(final Component displayName, final Mytems mytems) {
        this.displayName = displayName;
        this.mytems = mytems;
    }

    /**
     * @true if successful, false otherwise.
     */
    public boolean hold(Player player, boolean offHand) {
        ItemStack itemStack = offHand
            ? player.getInventory().getItemInOffHand()
            : player.getInventory().getItemInMainHand();
        if (itemStack != null && itemStack.getAmount() != 0) {
            return false;
        }
        // Helmet slot is empty now!
        itemStack = mytems.getMytem().createItemStack(player);
        if (offHand) {
            player.getInventory().setItemInOffHand(itemStack);
        } else {
            player.getInventory().setItemInMainHand(itemStack);
        }
        return true;
    }

    /**
     * Remove any handheld from the player.
     * @return the Handheld that was removed or false
     */
    public static Handheld remove(Player player, boolean offHand) {
        // Silently remove all non-held slots
        int heldItemSlot = player.getInventory().getHeldItemSlot();
        for (int i = 0; i < 9; i += 1) {
            if (i == heldItemSlot) continue;
            ItemStack itemStack = player.getInventory().getItem(i);
            Handheld handheld = of(itemStack);
            if (handheld != null) {
                player.getInventory().setItem(i, null);
            }
        }
        // Now do the actual hand
        Handheld handheld = of(player, offHand);
        if (handheld == null) return null;
        if (offHand) {
            player.getInventory().setItemInOffHand(null);
        } else {
            player.getInventory().setItemInMainHand(null);
        }
        return handheld;
    }

    /**
     * Get the handheld that the player is currently wearing.
     * @return the worn Handheld or null.
     */
    public static Handheld of(Player player, boolean offHand) {
        ItemStack itemStack = offHand
            ? player.getInventory().getItemInOffHand()
            : player.getInventory().getItemInMainHand();
        if (itemStack == null || itemStack.getType() == Material.AIR) return null;
        Mytems mytems = Mytems.forItem(itemStack);
        if (mytems == null) return null;
        for (Handheld handheld : Handheld.values()) {
            if (handheld.mytems == mytems) return handheld;
        }
        return null;
    }

    public static Handheld of(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) return null;
        Mytems mytems = Mytems.forItem(itemStack);
        if (mytems == null) return null;
        for (Handheld handheld : Handheld.values()) {
            if (handheld.mytems == mytems) return handheld;
        }
        return null;
    }

    public ItemStack toItemStack() {
        ItemStack itemStack = mytems.getMytem().createItemStack();
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(displayName.decoration(TextDecoration.ITALIC, false));
        TextColor yellow = TextColor.color(0xFFFF00);
        TextColor gray = TextColor.color(0x808080);
        meta.lore(Arrays.asList(Component.text()
                                .append(Component.text("Left click", yellow))
                                .append(Component.text(" to hold in main hand", gray))
                                .decoration(TextDecoration.ITALIC, false).build(),
                                Component.text()
                                .append(Component.text("Right click", yellow))
                                .append(Component.text(" to hold in off hand", gray))
                                .decoration(TextDecoration.ITALIC, false).build()));
        meta.addItemFlags(ItemFlag.values());
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
