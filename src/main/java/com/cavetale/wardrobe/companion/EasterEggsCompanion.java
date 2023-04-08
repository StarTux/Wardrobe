package com.cavetale.wardrobe.companion;

import com.cavetale.mytems.Mytems;
import com.cavetale.mytems.util.Entities;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import static com.cavetale.wardrobe.WardrobePlugin.plugin;

public final class EasterEggsCompanion implements Companion {
    private Player player;
    private BukkitTask task;
    private List<Location> locations = new ArrayList<>();
    private List<ItemDisplay> entities = new ArrayList<>();
    private static final int EASTER_EGG_FACTOR = 5;
    private static final List<Mytems> EASTER_EGG_MYTEMS = List.of(Mytems.BLUE_EASTER_EGG,
                                                                  Mytems.GREEN_EASTER_EGG,
                                                                  Mytems.ORANGE_EASTER_EGG,
                                                                  Mytems.PINK_EASTER_EGG,
                                                                  Mytems.PURPLE_EASTER_EGG,
                                                                  Mytems.YELLOW_EASTER_EGG);

    @Override
    public CompanionType getType() {
        return CompanionType.EASTER_EGGS;
    }

    @Override
    public void start(final Player thePlayer) {
        this.player = thePlayer;
        task = Bukkit.getScheduler().runTaskTimer(plugin(), this::tick, 1L, 1L);
        spawnEntities();
    }

    @Override
    public void tick() {
        boolean entityIsInvalid = false;
        for (var entity : entities) {
            if (!entity.isValid()) {
                entityIsInvalid = true;
                break;
            }
        }
        if (entityIsInvalid) {
            clearEntities();
            spawnEntities();
            return;
        }
        Location oldLocation = locations.get(0);
        if (!oldLocation.getWorld().equals(player.getWorld())) {
            clearEntities();
            spawnEntities();
            return;
        }
        Location newLocation = player.getLocation();
        if (newLocation.distance(oldLocation) < 0.125) return;
        locations.add(0, newLocation);
        locations.remove(locations.size() - 1);
        for (int i = 0; i < entities.size(); i += 1) {
            int j = (i + 1) * EASTER_EGG_FACTOR - 1;
            entities.get(i).teleport(locations.get(j));
        }
    }

    @Override
    public void stop() {
        clearEntities();
        task.cancel();
    }

    private void spawnEntities() {
        Location location = player.getLocation();
        for (int i = 0; i < EASTER_EGG_MYTEMS.size() * EASTER_EGG_FACTOR; i += 1) {
            locations.add(location);
        }
        for (int i = 0; i < EASTER_EGG_MYTEMS.size(); i += 1) {
            Mytems mytems = EASTER_EGG_MYTEMS.get(i);
            ItemDisplay entity = location.getWorld().spawn(location, ItemDisplay.class, e -> {
                    e.setPersistent(false);
                    Entities.setTransient(e);
                    e.setBillboard(ItemDisplay.Billboard.CENTER);
                    e.setItemStack(mytems.createItemStack());
                    e.setBrightness(new ItemDisplay.Brightness(15, 15));
                    e.setTransformation(new Transformation(new Vector3f(0f, 0.5f, 0f),
                                                           new AxisAngle4f(0f, 0f, 0f, 0f),
                                                           new Vector3f(1f, 1f, 0f),
                                                           new AxisAngle4f(0f, 0f, 0f, 0f)));
                    e.setShadowStrength(1.0f);
                    e.setShadowRadius(0.5f);
                });
            if (entity == null) break;
            entities.add(entity);
        }
        Collections.shuffle(entities);
    }

    private void clearEntities() {
        for (var it : entities) it.remove();
        entities.clear();
        locations.clear();
    }
}
