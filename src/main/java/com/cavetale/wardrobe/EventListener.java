package com.cavetale.wardrobe;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public final class EventListener implements Listener {
    private final WardrobePlugin plugin;
    public static final int COLOR = 0x4169E1;

    void enable() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.LOWEST)
    void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null || !clickedInventory.equals(player.getInventory())) return;
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || itemStack.getAmount() == 0) return;
        Hat hat = Hat.of(itemStack);
        if (hat != null) {
            event.setCancelled(true);
            event.setCurrentItem(null);
            player.sendMessage(Component.text("Hat removed: ").color(TextColor.color(COLOR))
                               .append(hat.displayName));
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1.0f, 1.0f);
            return;
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
            Hat hat = Hat.of(itemStack);
            if (hat != null) {
                event.setCancelled(true);
                event.getView().setItem(rawSlot, null);
                player.sendMessage(Component.text("Hat removed: ").color(TextColor.color(COLOR))
                                   .append(hat.displayName));
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1.0f, 1.0f);
                continue;
            }
        }
    }

    @EventHandler
    void onPlayerQuit(PlayerQuitEvent event) {
        plugin.removeAll(event.getPlayer());
    }
}
