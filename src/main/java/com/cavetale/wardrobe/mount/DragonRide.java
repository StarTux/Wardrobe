package com.cavetale.wardrobe.mount;

import com.cavetale.mytems.Mytems;
import com.cavetale.wardrobe.Mount;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderDragonPart;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import static com.cavetale.mytems.util.Collision.collidesWithBlock;
import static com.cavetale.wardrobe.WardrobePlugin.plugin;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public final class DragonRide extends Ride {
    private final Player player;
    private final EnderDragon dragon;
    private final ArmorStand armorStand;
    private State state = State.PAUSE;
    private float yaw;

    public DragonRide(final Player player, final EnderDragon dragon, final ArmorStand armorStand, final Mount mount) {
        super(player.getUniqueId(), mount);
        this.player = player;
        this.dragon = dragon;
        this.armorStand = armorStand;
    }

    public DragonRide of(EnderDragon entity) {
        return ENTITY_MAP.get(entity.getUniqueId()) instanceof DragonRide dragonRide
            ? dragonRide
            : null;
    }

    public DragonRide of(EnderDragonPart part) {
        return ENTITY_MAP.get(part.getUniqueId()) instanceof DragonRide dragonRide
            ? dragonRide
            : null;
    }

    @Override
    protected void onEnable() {
        ENTITY_MAP.put(dragon.getUniqueId(), this);
        ENTITY_MAP.put(armorStand.getUniqueId(), this);
        for (var part : dragon.getParts()) {
            ENTITY_MAP.put(part.getUniqueId(), this);
        }
        Bukkit.getScheduler().runTaskLater(plugin(), () -> {
                player.sendActionBar(textOfChildren(Mytems.MOUSE_RIGHT, text(" Start or stop flying (item in hand required)", AQUA)));
                player.sendMessage(textOfChildren(Mytems.MOUSE_RIGHT, text(" Start or stop flying (item in hand required)", AQUA)));
            }, 2L);
        yaw = player.getLocation().getYaw();
    }

    @Override
    protected void onCancel() {
        ENTITY_MAP.remove(dragon.getUniqueId());
        for (var part : dragon.getParts()) {
            ENTITY_MAP.remove(part.getUniqueId());
        }
        ENTITY_MAP.remove(armorStand.getUniqueId());
        dragon.remove();
        armorStand.remove();
    }

    private boolean failed;
    private final Vector upVector = new Vector(0.0, 1.5, 0.0);

    @Override
    protected void tick() {
        if (dragon.isDead() || armorStand.isDead()
            || !player.isOnline() || !player.isValid()
            || !armorStand.equals(player.getVehicle()) || dragon.getLocation().getY() > 512.0) {
            cancel();
            return;
        }
        if (collidesWithBlock(dragon.getWorld(), dragon.getBoundingBox())) {
            cancel();
            player.sendActionBar(text("Your dragon crashed", RED));
            player.sendMessage(text("Your dragon crashed", RED));
            player.spawnParticle(Particle.EXPLOSION_HUGE, dragon.getLocation(), 1, 2.0, 2.0, 2.0, 0.0);
            player.playSound(dragon.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.MASTER, 1.0f, 1.0f);
            return;
        }
        dragon.setPhase(state.phase);
        Location ploc = player.getLocation();
        Location dloc = dragon.getLocation();
        Location aloc = armorStand.getLocation();
        final Vector dragonVector;
        switch (state) {
        case PAUSE:
            dragonVector = new Vector(0.0, 0.0, 0.0);
            break;
        case RISE:
            dragonVector = new Vector(0.0, 0.2, 0.0);
            break;
        case FLY:
            dragonVector = ploc.getDirection().normalize().multiply(new Vector(0.4, 0.2, 0.4));
            yaw = ploc.getYaw();
            break;
        default: throw new IllegalStateException(state.name());
        }
        dragon.setVelocity(dragonVector);
        final Vector armorStandVector = dloc.toVector().add(upVector).subtract(aloc.toVector()).add(dragonVector);
        armorStand.setVelocity(armorStandVector);
        armorStand.setRotation(ploc.getYaw(), 0.0f);
        if (!failed) {
            try {
                Object handle = dragon.getClass().getMethod("getHandle").invoke(dragon);
                handle.getClass().getMethod("p", float.class).invoke(handle, yaw + 180f);
            } catch (Exception e) {
                e.printStackTrace();
                failed = true;
            }
        }
    }

    @Override
    protected void onLeftClick() {
        click();
    }

    @Override
    protected void onRightClick() {
        click();
    }

    private void click() {
        State[] states = State.values();
        int newValue = (state.ordinal() + 1) % states.length;
        state = states[newValue];
        player.sendActionBar(state.infoText);
        player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, SoundCategory.MASTER, 1.0f, 1.0f);
    }

    @RequiredArgsConstructor
    private enum State {
        PAUSE(EnderDragon.Phase.HOVER, textOfChildren(Mytems.ENDER_DRAGON_FACE, text("Paused", DARK_PURPLE))),
        RISE(EnderDragon.Phase.CIRCLING, textOfChildren(Mytems.ENDER_DRAGON_FACE, text("Rising", LIGHT_PURPLE))),
        FLY(EnderDragon.Phase.LAND_ON_PORTAL, textOfChildren(Mytems.ENDER_DRAGON_FACE, text("Free Flight", LIGHT_PURPLE))),
        ;

        private final EnderDragon.Phase phase;
        private final Component infoText;
    }
}
