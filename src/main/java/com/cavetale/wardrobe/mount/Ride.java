package com.cavetale.wardrobe.mount;

import com.cavetale.wardrobe.Mount;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitTask;
import static com.cavetale.wardrobe.WardrobePlugin.plugin;

@RequiredArgsConstructor
public abstract class Ride {
    protected final UUID uuid; // player uuid
    protected final Mount mount;
    private BukkitTask task;
    /** Map player UUID to ride. */
    protected static final Map<UUID, Ride> RIDES = new HashMap<>();

    protected final void enable() {
        task = Bukkit.getScheduler().runTaskTimer(plugin(), this::tick, 0L, 1L);
        RIDES.put(uuid, this);
        onEnable();
    }

    public final void cancel() {
        if (task != null) {
            task.cancel();
            task = null;
        }
        RIDES.remove(uuid);
        onCancel();
    }

    public static void cancelAll() {
        for (Ride ride : List.copyOf(RIDES.values())) {
            ride.cancel();
        }
        RIDES.clear();
    }

    public static Ride of(Player player) {
        return RIDES.get(player.getUniqueId());
    }

    protected abstract void onEnable();

    protected abstract void onCancel();

    protected abstract void tick();

    protected abstract void onLeftClick();

    public final void onPlayerDamage(Player player, EntityDamageEvent event) {
        switch (event.getCause()) {
        case FALL:
        case SUFFOCATION:
            event.setCancelled(true);
            break;
        default: break;
        }
    }

    public final void onPlayerInteract(Player player, PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        switch (event.getAction()) {
        case LEFT_CLICK_BLOCK:
        case LEFT_CLICK_AIR:
            onLeftClick();
            break;
        default: break;
        }
    }
}
