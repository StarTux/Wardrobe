package com.cavetale.wardrobe;

import com.cavetale.mytems.Mytems;
import com.cavetale.wardrobe.util.Items;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextColor.color;

@Getter @RequiredArgsConstructor
public enum Handheld implements WardrobeItem {
    RED_LIGHTSABER(text("Red Laser Sword", color(0xff1d0b)), Mytems.RED_LIGHTSABER, true),
    BLUE_LIGHTSABER(text("Blue Laser Sword", color(0xadf3f3)), Mytems.BLUE_LIGHTSABER, true),
    CUPID_WINGS(text("Cupid Wings", color(0xFFA3C3)), Mytems.CUPID_WINGS, false),
    BUTTERFLY_WINGS(text("Butterfly Wings", color(0xADD8E6)), Mytems.BUTTERFLY_WINGS, false),
    HOOK_HAND(text("Hookhand", GRAY), Mytems.HOOK_HAND, true),
    ;

    public final Component displayName;
    public final Mytems mytems;
    public final boolean mainHandAllowed;

    @Override
    public Category getCategory() {
        return Category.HANDHELD;
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
        return Items.text(mytems.createItemStack(), List.of(displayName));
    }

    @Override
    public List<Component> getMenuTooltip() {
        return List.of(getDisplayName(),
                       text("Handheld", DARK_GRAY),
                       empty(),
                       textOfChildren(Mytems.MOUSE_LEFT, text(" Equip", GRAY)),
                       textOfChildren(Mytems.MOUSE_RIGHT, text(" Off-hand", GRAY)));
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event) {
        if (event.getClick() != ClickType.LEFT && event.getClick() != ClickType.RIGHT) return;
        boolean offHand = !mainHandAllowed || event.isRightClick();
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1.0f, 1.0f);
        if (this == remove(player, offHand)) {
            player.sendMessage(textOfChildren(text("Handheld removed: ", WardrobeCommand.COLOR), displayName));
            return;
        }
        if (hold(player, offHand)) {
            player.sendMessage(textOfChildren(text("Handheld equipped: ", WardrobeCommand.COLOR), displayName));
        } else {
            player.sendMessage(textOfChildren(text("Cannot equip ", color(0xFF0000)),
                                              displayName,
                                              text((offHand ? ": Hand is full!" : " Off-hand is full!"), color(0xFF0000))));
        }
    }

    @Override
    public boolean isWearing(Player player) {
        return of(player, false) == this
            || of(player, true) == this;
    }

    @Override
    public int getIndex() {
        return ordinal();
    }
}
