package com.cavetale.wardrobe.companion;

import com.cavetale.mytems.Mytems;
import com.cavetale.mytems.util.Entities;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import static com.cavetale.wardrobe.WardrobePlugin.plugin;

public abstract class ButterflyCompanion implements Companion {
    @Getter private final CompanionType type;
    private final Mytems mytems;
    private Particle.DustOptions dust;
    private Player player;
    private BukkitTask task;
    private ItemDisplay entity = null;
    private int ticks = 0;

    protected ButterflyCompanion(final CompanionType type, final Mytems mytems, final int hexColor) {
        this.type = type;
        this.mytems = mytems;
        this.dust = new Particle.DustOptions(Color.fromRGB(hexColor), 0.75f);
    }

    public static final class Blue extends ButterflyCompanion {
        Blue() {
            super(CompanionType.BLUE_BUTTERFLY, Mytems.BLUE_BUTTERFLY, 0x9595D8);
        }
    }

    public static final class Cyan extends ButterflyCompanion {
        Cyan() {
            super(CompanionType.CYAN_BUTTERFLY, Mytems.CYAN_BUTTERFLY, 0x399D9D);
        }
    }

    public static final class Green extends ButterflyCompanion {
        Green() {
            super(CompanionType.GREEN_BUTTERFLY, Mytems.GREEN_BUTTERFLY, 0x1DB91D);
        }
    }

    public static final class Orange extends ButterflyCompanion {
        Orange() {
            super(CompanionType.ORANGE_BUTTERFLY, Mytems.ORANGE_BUTTERFLY, 0xFF8000);
        }
    }

    public static final class Pink extends ButterflyCompanion {
        Pink() {
            super(CompanionType.PINK_BUTTERFLY, Mytems.PINK_BUTTERFLY, 0xD89595);
        }
    }

    public static final class Purple extends ButterflyCompanion {
        Purple() {
            super(CompanionType.PURPLE_BUTTERFLY, Mytems.PURPLE_BUTTERFLY, 0xD85FD8);
        }
    }

    public static final class Yellow extends ButterflyCompanion {
        Yellow() {
            super(CompanionType.YELLOW_BUTTERFLY, Mytems.YELLOW_BUTTERFLY, 0xD8D85F);
        }
    }

    @Override
    public final void start(final Player thePlayer) {
        this.player = thePlayer;
        task = Bukkit.getScheduler().runTaskTimer(plugin(), this::tick, 1L, 1L);
    }

    @Override
    public final void tick() {
        if (player.getGameMode() == GameMode.SPECTATOR) {
            removeEntity();
            return;
        } else if (entity == null || !entity.isValid() || !entity.getWorld().equals(player.getWorld())) {
            removeEntity();
            spawnEntity();
        }
        if (entity == null || entity.isDead()) return;
        Location loc = getLocation();
        entity.teleport(loc);
        if (ticks % 2 == 0) {
            loc.getWorld().spawnParticle(Particle.DUST, loc, 1, 0.0, 0.0, 0.0, 0.0, dust);
        }
        ticks += 1;
    }

    @Override
    public final void stop() {
        removeEntity();
        task.cancel();
    }

    private Location getLocation() {
        Location eye = player.getEyeLocation();
        final double dt = (double) ticks;
        double distance = 2.5 + Math.sin(dt * 0.1);
        double angle = dt * 0.03f;
        double x = Math.cos(angle) * distance;
        double z = Math.sin(angle) * distance;
        double y = Math.sin(dt * 0.3) * 0.5;
        float yaw = (float) Math.sin(dt * 0.05) * 180f;
        eye = eye.add(x, y, z);
        eye.setYaw(yaw);
        eye.setPitch(0.0f);
        return eye;
    }

    private void spawnEntity() {
        Location eye = getLocation();
        this.entity = eye.getWorld().spawn(eye, ItemDisplay.class, e -> {
                e.setPersistent(false);
                Entities.setTransient(e);
                //e.setBillboard(ItemDisplay.Billboard.VERTICAL);
                e.setItemStack(mytems.createItemStack());
                e.setBrightness(new ItemDisplay.Brightness(15, 15));
                e.setTransformation(new Transformation(new Vector3f(0f, 0.0f, 0f),
                                                       new AxisAngle4f(0f, 0f, 0f, 0f),
                                                       new Vector3f(0.75f, 0.75f, 0f),
                                                       new AxisAngle4f(0f, 0f, 0f, 0f)));
                e.setShadowStrength(0.25f);
                e.setShadowRadius(0.25f);
            });
    }

    private void removeEntity() {
        if (entity == null) return;
        entity.remove();
        entity = null;
    }
}
