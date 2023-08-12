package com.cavetale.wardrobe;

import com.cavetale.wardrobe.companion.CompanionType;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Abstraction for Hat, Handheld, and Costume.  The first two are
 * actual items.
 */
public interface WardrobeItem {
    static WardrobeItem of(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) return null;
        Hat hat = Hat.of(itemStack);
        if (hat != null) return hat;
        Handheld handheld = Handheld.of(itemStack);
        if (handheld != null) return handheld;
        FlagWardrobeItem flag = FlagWardrobeItem.of(itemStack);
        if (flag != null) return flag;
        return null;
    }

    Component getDisplayName();

    ItemStack toMenuItem();

    List<Component> getMenuTooltip();

    void onClick(Player player, InventoryClickEvent event);

    static List<WardrobeItem> all() {
        List<WardrobeItem> list = new ArrayList<>();
        list.addAll(List.of(Hat.values()));
        list.addAll(List.of(Handheld.values()));
        list.addAll(List.of(Mount.values()));
        list.addAll(FlagWardrobeItem.values());
        list.addAll(List.of(ShoulderEntity.values()));
        for (var it : CompanionType.values()) list.add(it);
        return list;
    }

    String name();

    Category getCategory();

    int getIndex();

    boolean isWearing(Player player);
}
