package com.cavetale.wardrobe.mount;

import com.cavetale.core.connect.ServerCategory;
import com.cavetale.core.event.block.PlayerBlockAbilityQuery;
import com.cavetale.mytems.util.Entities;
import com.cavetale.wardrobe.Mount;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

@RequiredArgsConstructor
public final class ArmorStandMount implements MountAdapter {
    protected final double speed;

    public MountResult mount(Player player, Mount mount) {
        if (!ServerCategory.current().isSurvival()) {
            return MountResult.SERVER;
        }
        if (player.isInsideVehicle()) return MountResult.ALREADY_MOUNTED;
        if (!PlayerBlockAbilityQuery.Action.FLY.query(player, player.getLocation().getBlock())) {
            return MountResult.LOCATION;
        }
        final ArmorStand armorStand = player.getWorld().spawn(player.getLocation(), ArmorStand.class, as -> {
                as.setPersistent(false);
                Entities.setTransient(as);
                as.getEquipment().setHelmet(mount.mytems.createItemStack());
                as.setInvisible(true);
                as.setSmall(false);
                as.setDisabledSlots(EquipmentSlot.values());
            });
        if (armorStand == null) return MountResult.UNKNOWN;
        armorStand.addPassenger(player);
        new ArmorStandMountRide(player, armorStand, this, mount).enable();
        return MountResult.SUCCESS;
    }

    public boolean unmount(Player player, Mount mount) {
        if (Ride.of(player) instanceof ArmorStandMountRide ride && ride.mount == mount) {
            ride.cancel();
            return true;
        } else {
            return false;
        }
    }
}
