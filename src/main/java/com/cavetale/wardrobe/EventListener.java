package com.cavetale.wardrobe;

import com.cavetale.core.connect.NetworkServer;
import com.cavetale.core.event.entity.PlayerEntityAbilityQuery;
import com.cavetale.mytems.Mytems;
import com.cavetale.mytems.util.Gui;
import com.cavetale.wardrobe.mount.Ride;
import io.papermc.paper.event.entity.EntityPushedByEntityAttackEvent;
import java.util.UUID;
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
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import static net.kyori.adventure.text.Component.newline;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.event.ClickEvent.runCommand;
import static net.kyori.adventure.text.event.HoverEvent.showText;
import static net.kyori.adventure.text.format.TextColor.color;

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
        if (gui != null && gui.getPlugin() == plugin && clickedInventory.equals(gui.getInventory())) return;
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
            Ride ride = Ride.ofPlayer(player);
            if (ride != null) ride.onPlayerDamage(player, event);
        } else if (Ride.ofEntity(event.getEntity()) != null) {
            event.setCancelled(true);
        }
    }

    private UUID thornsId;

    @EventHandler(priority = EventPriority.LOWEST)
    private void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (Ride.ofEntity(event.getDamager()) != null) {
            event.setCancelled(true);
        }
        if (Ride.ofEntity(event.getEntity()) != null) {
            event.setCancelled(true);
            if (event.getCause() == EntityDamageEvent.DamageCause.THORNS) {
                UUID uuid = event.getDamager().getUniqueId();
                thornsId = uuid;
                Bukkit.getScheduler().runTask(plugin, () -> {
                        if (uuid.equals(thornsId)) thornsId = null;
                    });
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    /**
     * Thorns damage is still applied to player items even if the
     * damage event has been cancelled.  Therefore we remember the
     * last player who dealt damage to the dragon via thorns (see
     * above) and cancel it here.
     */
    private void onPlayerItemDamage(PlayerItemDamageEvent event) {
        if (thornsId != null && event.getPlayer().getUniqueId().equals(thornsId)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (Ride.ofEntity(event.getEntity()) != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onEntityPushedByEntityAttack(EntityPushedByEntityAttackEvent event) {
        if (Ride.ofEntity(event.getPushedBy()) != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onEntityExplode(EntityExplodeEvent event) {
        if (Ride.ofEntity(event.getEntity()) != null) {
            event.setCancelled(true);
            event.blockList().clear();
        }
    }

    @EventHandler
    private void onPlayerEntityAbility(PlayerEntityAbilityQuery query) {
        if (Ride.ofEntity(query.getEntity()) != null) {
            query.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = false)
    private void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Ride ride = Ride.ofPlayer(player);
        if (ride != null) ride.onPlayerInteract(player, event);
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        if (!NetworkServer.HUB.isThisServer() && !NetworkServer.BETA.isThisServer()) return;
        Player player = event.getPlayer();
        if (!player.hasPermission("wardrobe.ad")) return;
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (!player.isOnline()) return;
                player.sendMessage(textOfChildren(newline(),
                                                  Mytems.MOUSE_LEFT,
                                                  text(" Does your heart ", color(0xFFA3C3)),
                                                  text("beat for someone?", color(0xFFA3C3)),
                                                  newline())
                                   .hoverEvent(showText(textOfChildren(Mytems.HEART, text(" This heart beats for you", color(0xFFA3C3)))))
                                   .clickEvent(runCommand("/wardrobe click")));
            }, 200L);
    }

    @EventHandler
    private void onShoulderEntitySpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SHOULDER_ENTITY) return;
        if (ShoulderEntity.of(event.getEntity()) == null) return;
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        switch (event.getNewGameMode()) {
        case SPECTATOR:
            ShoulderEntity.remove(player, false);
            ShoulderEntity.remove(player, true);
            Gui gui = Gui.of(player);
            if (gui != null && gui.getPlugin() == plugin) player.closeInventory();
            break;
        case CREATIVE:
            ShoulderEntity.remove(player, false);
            ShoulderEntity.remove(player, true);
            break;
        default: break;
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        ShoulderEntity.remove(player, false);
        ShoulderEntity.remove(player, true);
        Gui gui = Gui.of(player);
        if (gui != null && gui.getPlugin() == plugin) player.closeInventory();
    }
}
