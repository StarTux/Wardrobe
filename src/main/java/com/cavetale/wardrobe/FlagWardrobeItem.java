package com.cavetale.wardrobe;

import com.cavetale.mytems.Mytems;
import com.cavetale.mytems.MytemsCategory;
import com.winthier.title.Title;
import com.winthier.title.TitlePlugin;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import static com.cavetale.core.util.CamelCase.toCamelCase;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.NamedTextColor.*;

@RequiredArgsConstructor
public final class FlagWardrobeItem implements WardrobeItem {
    private final Title title;
    private final Mytems mytems;

    public static List<FlagWardrobeItem> values() {
        List<FlagWardrobeItem> result = new ArrayList<>();
        for (Title title : TitlePlugin.getInstance().getTitles()) {
            Mytems mytems = title.getMytems();
            if (mytems == null) continue;
            if (mytems.category != MytemsCategory.COUNTRY_FLAG && mytems.category != MytemsCategory.PRIDE_FLAGS) continue;
            result.add(new FlagWardrobeItem(title, mytems));
        }
        return result;
    }

    public static FlagWardrobeItem of(ItemStack item) {
        Mytems mytems = Mytems.forItem(item);
        if (mytems == null) return null;
        if (mytems.category != MytemsCategory.COUNTRY_FLAG && mytems.category != MytemsCategory.PRIDE_FLAGS) return null;
        for (Title title : TitlePlugin.getInstance().getTitles()) {
            if (title.getMytems() == mytems) {
                return new FlagWardrobeItem(title, mytems);
            }
        }
        return null;
    }

    public boolean has(Player player) {
        var session = TitlePlugin.getInstance().findSession(player);
        return session != null && session.hasTitle(title);
    }

    @Override
    public Component getDisplayName() {
        return text(title.getName());
    }

    @Override
    public ItemStack toMenuItem() {
        return mytems.createIcon();
    }

    @Override
    public List<Component> getMenuTooltip() {
        return List.of(getDisplayName(),
                       text("Flag", DARK_GRAY),
                       empty(),
                       textOfChildren(Mytems.MOUSE_LEFT, text(" Wear on offhand sleeve", GRAY)),
                       textOfChildren(Mytems.MOUSE_RIGHT, text(" Wear on main sleeve ", GRAY)),
                       textOfChildren(Mytems.SHIFT_KEY, Mytems.MOUSE_LEFT, text(" Wear on head", GRAY)));
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event) {
        final EquipmentSlot slot = switch (event.getClick()) {
        case LEFT -> EquipmentSlot.OFF_HAND;
        case RIGHT -> EquipmentSlot.HAND;
        case SHIFT_LEFT -> EquipmentSlot.HEAD;
        default -> null;
        };
        if (slot == null) return;
        ItemStack oldItem = player.getEquipment().getItem(slot);
        if (oldItem != null && !oldItem.getType().isAir()) {
            FlagWardrobeItem wearing = of(oldItem);
            if (wearing != null) {
                player.getEquipment().setItem(slot, null);
                if (wearing.mytems == mytems) {
                    player.sendMessage(textOfChildren(text("Flag removed: ", WardrobeCommand.COLOR), getDisplayName()));
                    player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, SoundCategory.MASTER, 1.0f, 0.5f);
                    return;
                }
                player.getEquipment().setItem(slot, null);
            } else {
                player.sendMessage(textOfChildren(text("Cannot equip ", RED),
                                                  getDisplayName(),
                                                  text(" because " + toCamelCase(" ", slot) + " is full!", RED)));
                player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, SoundCategory.MASTER, 1.0f, 0.5f);
                return;
            }
        }
        player.getEquipment().setItem(slot, mytems.createItemStack());
        player.sendMessage(textOfChildren(text("Flag equipped: ", WardrobeCommand.COLOR), getDisplayName()));
        player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, SoundCategory.MASTER, 1.0f, 1.0f);
    }

    @Override
    public String name() {
        return mytems.name();
    }

    @Override
    public Category getCategory() {
        return Category.FLAG;
    }

    @Override
    public int ordinal() {
        return mytems.ordinal();
    }

    @Override
    public boolean isWearing(Player player) {
        return isWearing(player, EquipmentSlot.HAND)
            || isWearing(player, EquipmentSlot.OFF_HAND)
            || isWearing(player, EquipmentSlot.HEAD);
    }

    public boolean isWearing(Player player, EquipmentSlot slot) {
        ItemStack item = player.getEquipment().getItem(slot);
        if (item == null) return false;
        FlagWardrobeItem flag = of(item);
        return flag != null && flag.mytems == mytems;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof FlagWardrobeItem it && it.mytems == mytems;
    }

    @Override
    public int hashCode() {
        return mytems.hashCode();
    }
}
