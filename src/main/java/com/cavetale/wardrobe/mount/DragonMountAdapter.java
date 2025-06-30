package com.cavetale.wardrobe.mount;

import com.cavetale.core.connect.ServerCategory;
import com.cavetale.core.event.block.PlayerBlockAbilityQuery;
import com.cavetale.mytems.util.Entities;
import com.cavetale.wardrobe.Mount;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import static com.cavetale.mytems.util.Collision.collidesWithBlock;

public final class DragonMountAdapter implements MountAdapter {
    public MountResult mount(Player player, Mount mount) {
        if (!ServerCategory.current().isSurvival()) {
            return MountResult.SERVER;
        }
        if (player.isInsideVehicle()) return MountResult.ALREADY_MOUNTED;
        if (!PlayerBlockAbilityQuery.Action.FLY.query(player, player.getLocation().getBlock())) {
            return MountResult.LOCATION;
        }
        Location spawnLocation = player.getLocation().add(0.0, 1.0, 0.0);
        EnderDragon dragon = player.getWorld().spawn(spawnLocation, EnderDragon.class, e -> {
                e.setPersistent(false);
                Entities.setTransient(e);
                e.setCollidable(false);
                e.setSilent(true);
                if (collidesWithBlock(spawnLocation.getWorld(), e.getBoundingBox())) {
                    e.remove();
                }
            });
        if (dragon == null) return MountResult.UNKNOWN;
        if (dragon.isDead()) return MountResult.BLOCK_COLLISION;
        ArmorStand armorStand = player.getWorld().spawn(spawnLocation, ArmorStand.class, e -> {
                e.setPersistent(false);
                Entities.setTransient(e);
                e.setInvisible(true);
                e.setSmall(false);
                e.setDisabledSlots(EquipmentSlot.HAND, EquipmentSlot.OFF_HAND, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);
            });
        if (armorStand == null) {
            dragon.remove();
            return MountResult.UNKNOWN;
        }
        dragon.setPhase(EnderDragon.Phase.HOVER);
        Bukkit.getMobGoals().removeAllGoals(dragon);
        armorStand.addPassenger(player);
        new DragonRide(player, dragon, armorStand, mount).enable();
        return MountResult.SUCCESS;
    }

    public boolean unmount(Player player, Mount mount) {
        if (Ride.ofPlayer(player) instanceof DragonRide ride && ride.mount == mount) {
            ride.cancel();
            return true;
        } else {
            return false;
        }
    }
}
