package com.cavetale.wardrobe;

import com.cavetale.mytems.Mytems;
import com.cavetale.wardrobe.util.Items;
import java.util.List;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

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
        itemStack = mytems.getMytem().createItemStack();
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

    @Override
    public ItemStack toMenuItem() {
        ItemStack itemStack = mytems.getMytem().createItemStack();
        TextColor yellow = TextColor.color(0xFFFF00);
        itemStack.editMeta(meta -> {
                Items.text(meta, List.of(displayName,
                                         Component.text("Hand Item", NamedTextColor.DARK_PURPLE),
                                         Component.text("Left click to hold in main hand", yellow),
                                         Component.text("Right click to hold in off hand", yellow)));
            });
        return itemStack;
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event) {
        if (event.getClick() != ClickType.LEFT && event.getClick() != ClickType.RIGHT) return;
        boolean offHand = event.isRightClick();
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1.0f, 1.0f);
        if (this == remove(player, offHand)) {
            player.sendMessage(Component.text("Handheld removed: ").color(WardrobeCommand.COLOR)
                               .append(displayName));
            return;
        }
        if (hold(player, offHand)) {
            player.sendMessage(Component.text("Handheld equipped: ").color(WardrobeCommand.COLOR)
                               .append(displayName));
        } else {
            player.sendMessage(Component.text("Cannot equip ").color(TextColor.color(0xFF0000))
                               .append(displayName)
                               .append(Component.text(": Hand is full!").color(TextColor.color(0xFF0000))));
        }
    }
}
