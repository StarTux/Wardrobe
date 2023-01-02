package com.cavetale.wardrobe;

import com.cavetale.wardrobe.mount.ArmorStandMountRide;
import com.cavetale.wardrobe.mount.Ride;
import com.cavetale.wardrobe.util.Gui;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public final class EventListener implements Listener {
    private final WardrobePlugin plugin;
    public static final TextColor COLOR = WardrobeCommand.COLOR;

    void enable() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.LOWEST)
    void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) return;
        // The Wardrobe GUI is exempt.
        Gui gui = Gui.of(player);
        if (gui != null && clickedInventory.equals(gui.getInventory())) return;
        // When a WardrobeItem was clicked in the inventory, remove it!
        ItemStack clickedItem = event.getCurrentItem();
        WardrobeItem clickedWardrobeItem = WardrobeItem.of(clickedItem);
        if (clickedWardrobeItem != null) {
            event.setCancelled(true);
            event.setCurrentItem(null);
            player.sendMessage(Component.text("Wardrobe item removed: ").color(COLOR)
                               .append(clickedWardrobeItem.getDisplayName()));
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1.0f, 1.0f);
        }
        // In case of a swap action, do the same to the other end.
        switch (event.getAction()) {
        case HOTBAR_SWAP:
        case HOTBAR_MOVE_AND_READD:
            switch (event.getClick()) {
            case NUMBER_KEY: {
                int hotbarSlot = event.getHotbarButton();
                ItemStack hotbarItem = player.getInventory().getItem(hotbarSlot);
                WardrobeItem hotbarWardrobeItem = WardrobeItem.of(hotbarItem);
                if (hotbarWardrobeItem != null) {
                    event.setCancelled(true);
                    player.getInventory().setItem(hotbarSlot, null);
                    player.sendMessage(Component.text("Wardrobe item removed: ").color(COLOR)
                                       .append(hotbarWardrobeItem.getDisplayName()));
                }
                break;
            }
            case SWAP_OFFHAND:
                ItemStack offhandItem = player.getInventory().getItemInOffHand();
                WardrobeItem offhandWardrobeItem = WardrobeItem.of(offhandItem);
                if (offhandWardrobeItem != null) {
                    event.setCancelled(true);
                    player.getInventory().setItemInOffHand(null);
                    player.sendMessage(Component.text("Wardrobe item removed: ").color(COLOR)
                                       .append(offhandWardrobeItem.getDisplayName()));
                }
                break;
            default: break;
            }
        default: break;
        }
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.LOWEST)
    void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        for (int rawSlot : event.getRawSlots()) {
            Inventory clickedInventory = event.getView().getInventory(rawSlot);
            if (clickedInventory == null || !clickedInventory.equals(player.getInventory())) continue;
            ItemStack itemStack = event.getView().getItem(rawSlot);
            if (itemStack == null || itemStack.getAmount() == 0) continue;
            WardrobeItem wardrobeItem = WardrobeItem.of(itemStack);
            if (wardrobeItem != null) {
                event.setCancelled(true);
                event.getView().setItem(rawSlot, null);
                player.sendMessage(Component.text("Wardrobe item removed: ").color(COLOR)
                                   .append(wardrobeItem.getDisplayName()));
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1.0f, 1.0f);
                continue;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    void onPlayerQuit(PlayerQuitEvent event) {
        plugin.removeAll(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    private void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            Ride ride = Ride.of(player);
            if (ride != null) ride.onPlayerDamage(player, event);
        } else if (event.getEntity() instanceof ArmorStand armorStand) {
            ArmorStandMountRide ride = ArmorStandMountRide.of(armorStand);
            if (ride != null) ride.onArmorStandDamage(armorStand, event);
        }
    }

    @EventHandler(ignoreCancelled = false)
    private void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Ride ride = Ride.of(player);
        if (ride != null) ride.onPlayerInteract(player, event);
    }
}
