package com.cavetale.wardrobe;

import com.cavetale.mytems.Mytems;
import com.cavetale.mytems.util.Entities;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import static com.cavetale.wardrobe.WardrobePlugin.plugin;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextColor.color;

@Getter @RequiredArgsConstructor
public enum ShoulderEntity implements WardrobeItem {
    BLUE_PARROT(text("Blue Parrot", BLUE), Parrot.Variant.BLUE),
    CYAN_PARROT(text("Cyan Parrot", DARK_AQUA), Parrot.Variant.CYAN),
    GRAY_PARROT(text("Gray Parrot", GRAY), Parrot.Variant.GRAY),
    GREEN_PARROT(text("Green Parrot", GREEN), Parrot.Variant.GREEN),
    RED_PARROT(text("Red Parrot", RED), Parrot.Variant.RED),
    ;

    public final Component displayName;
    public final Parrot.Variant parrotVariant;

    @Override
    public Category getCategory() {
        return Category.COMPANION;
    }

    /**
     * @true if successful, false otherwise.
     */
    public boolean hold(Player player, boolean left) {
        Entity entity = left
            ? player.getShoulderEntityLeft()
            : player.getShoulderEntityRight();
        if (entity != null) {
            return false;
        }
        Parrot parrot = player.getWorld().spawn(player.getLocation(), Parrot.class, e -> {
                e.setPersistent(false);
                Entities.setTransient(e);
                e.getPersistentDataContainer().set(getNamespacedKey(), PersistentDataType.STRING, name());
                e.setVariant(parrotVariant);
                e.setAdult();
                e.setAgeLock(true);
            });
        if (left) {
            player.setShoulderEntityLeft(parrot);
        } else {
            player.setShoulderEntityRight(parrot);
        }
        return true;
    }

    private static NamespacedKey getNamespacedKey() {
        return new NamespacedKey(plugin(), "shoulder_entity");
    }

    /**
     * Remove any shoulder entity from the player.
     * @return the ShoulderEntity that was removed or false
     */
    public static ShoulderEntity remove(Player player, boolean left) {
        ShoulderEntity shoulderEntity = of(player, left);
        if (shoulderEntity == null) return null;
        if (left) {
            player.setShoulderEntityLeft(null);
        } else {
            player.setShoulderEntityRight(null);
        }
        return shoulderEntity;
    }

    /**
     * Get the shoulder entity that the player is currently wearing.
     * @return the worn ShoulderEntity or null.
     */
    public static ShoulderEntity of(Player player, boolean left) {
        Entity entity = left
            ? player.getShoulderEntityLeft()
            : player.getShoulderEntityRight();
        return of(entity);
    }

    public static ShoulderEntity of(Entity entity) {
        if (entity == null) return null;
        if (!entity.getPersistentDataContainer().has(getNamespacedKey(), PersistentDataType.STRING)) return null;
        String value = entity.getPersistentDataContainer().get(getNamespacedKey(), PersistentDataType.STRING);
        try {
            return ShoulderEntity.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException iae) {
            return null;
        }
    }

    @Override
    public ItemStack toMenuItem() {
        return new ItemStack(Material.PARROT_SPAWN_EGG);
    }

    @Override
    public List<Component> getMenuTooltip() {
        return List.of(getDisplayName(),
                       empty(),
                       textOfChildren(Mytems.MOUSE_LEFT, text(" Left Shoulder", GRAY)),
                       textOfChildren(Mytems.MOUSE_RIGHT, text(" Right Shoulder", GRAY)));
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event) {
        if (event.getClick() != ClickType.LEFT && event.getClick() != ClickType.RIGHT) return;
        final boolean left = event.isLeftClick();
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1.0f, 1.0f);
        if (this == remove(player, left)) {
            player.sendMessage(textOfChildren(text("Removed: ", WardrobeCommand.COLOR), displayName));
            return;
        }
        if (hold(player, left)) {
            player.sendMessage(textOfChildren(text("Equipped: ", WardrobeCommand.COLOR), displayName));
        } else {
            player.sendMessage(textOfChildren(text("Cannot carry ", color(0xFF0000)),
                                              displayName,
                                              text((left ? ": Left shoulder is occupied!" : " Right shoulder is occupied!"), color(0xFF0000))));
        }
    }

    @Override
    public boolean isWearing(Player player) {
        return of(player, false) == this
            || of(player, true) == this;
    }

    @Override
    public int getIndex() {
        return 100 + ordinal();
    }
}
