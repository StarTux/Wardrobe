package com.cavetale.wardrobe.mount;

import com.cavetale.core.event.block.PlayerBlockAbilityQuery;
import com.cavetale.mytems.Mytems;
import com.cavetale.wardrobe.Mount;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import static com.cavetale.wardrobe.WardrobePlugin.plugin;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public final class ArmorStandMountRide extends Ride {
    private final Player player;
    private final ArmorStand armorStand;
    private final ArmorStandMount adapter;
    private int x;
    private int y;
    private int z;
    private boolean flying;

    public ArmorStandMountRide(final Player player, final ArmorStand armorStand, final ArmorStandMount adapter, final Mount mount) {
        super(player.getUniqueId(), mount);
        this.player = player;
        this.armorStand = armorStand;
        this.adapter = adapter;
    }

    @Override
    protected void onEnable() {
        Bukkit.getScheduler().runTaskLater(plugin(), () -> {
                player.sendActionBar(textOfChildren(Mytems.MOUSE_LEFT, text(" Start or stop flying", AQUA)));
                player.sendMessage(textOfChildren(Mytems.MOUSE_LEFT, text(" Start or stop flying", AQUA)));
            }, 2L);
    }

    @Override
    protected void onCancel() {
        armorStand.remove();
    }

    @Override
    protected void tick() {
        if (armorStand.isDead() || !player.isOnline() || !player.isValid() || !armorStand.equals(player.getVehicle())) {
            cancel();
            return;
        }
        Location playerLocation = player.getLocation();
        if (x != playerLocation.getBlockX() || y != playerLocation.getBlockY() || z != playerLocation.getBlockZ()) {
            if (y > player.getWorld().getMaxHeight() + 16 || y < player.getWorld().getMinHeight() - 16
                || !player.getWorld().getWorldBorder().isInside(playerLocation)
                || !PlayerBlockAbilityQuery.Action.FLY.query(player, player.getLocation().getBlock())) {
                cancel();
                return;
            }
            x = playerLocation.getBlockX();
            y = playerLocation.getBlockY();
            z = playerLocation.getBlockZ();
        }
        armorStand.setRotation(playerLocation.getYaw(), 0.0f);
        if (flying) {
            Vector lookAt = playerLocation.getDirection();
            armorStand.setVelocity(lookAt.normalize().multiply(adapter.speed).multiply(new Vector(1.0f, 0.5f, 1.0f)));
        } else {
            armorStand.setVelocity(new Vector().zero());
        }
        player.setFallDistance(0);
    }

    @Override
    protected void onLeftClick() {
        setFlying(!flying);
        if (flying) {
            player.getWorld().playSound(player.getLocation(), Sound.UI_LOOM_TAKE_RESULT, SoundCategory.MASTER, 1.0f, 1.5f);
        } else {
            player.getWorld().playSound(player.getLocation(), Sound.UI_STONECUTTER_TAKE_RESULT, SoundCategory.MASTER, 1.0f, 0.8f);
        }
    }

    private void setFlying(final boolean value) {
        flying = value;
        if (!flying) {
            armorStand.setGravity(false);
            armorStand.setCanMove(false);
            armorStand.setCanTick(false);
        } else {
            armorStand.setGravity(true);
            armorStand.setCanMove(true);
            armorStand.setCanTick(true);
        }
    }
}
