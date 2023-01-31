package com.cavetale.wardrobe.mount;

import com.cavetale.wardrobe.Mount;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitTask;
import static com.cavetale.wardrobe.WardrobePlugin.plugin;

@Getter @RequiredArgsConstructor
public abstract class Ride {
    protected final UUID uuid; // player uuid
    protected final Mount mount;
    private BukkitTask task;
    /** Map player UUID to ride. */
    protected static final Map<UUID, Ride> PLAYER_MAP = new HashMap<>();
    /** Mounted entities or components. */
    protected static final Map<UUID, Ride> ENTITY_MAP = new HashMap<>();

    protected final void enable() {
        task = Bukkit.getScheduler().runTaskTimer(plugin(), this::tick, 0L, 1L);
        PLAYER_MAP.put(uuid, this);
        onEnable();
    }

    public final void cancel() {
        if (task != null) {
            task.cancel();
            task = null;
        }
        PLAYER_MAP.remove(uuid);
        onCancel();
    }

    public static void cancelAll() {
        for (Ride ride : List.copyOf(PLAYER_MAP.values())) {
            ride.cancel();
        }
        PLAYER_MAP.clear();
    }

    public static Ride ofPlayer(Player player) {
        return PLAYER_MAP.get(player.getUniqueId());
    }

    public static Ride ofEntity(Entity entity) {
        return ENTITY_MAP.get(entity.getUniqueId());
    }

    protected abstract void onEnable();

    protected abstract void onCancel();

    protected abstract void tick();

    protected void onLeftClick() { }

    protected void onRightClick() { }

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
        case RIGHT_CLICK_BLOCK:
        case RIGHT_CLICK_AIR:
            onRightClick();
            break;
        default: break;
        }
    }
}
