package com.cavetale.wardrobe;

import com.cavetale.mytems.Mytems;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.NamedTextColor.*;

@RequiredArgsConstructor
/*
 * All the categories displayed in the menu.
 */
public enum Category {
    ALL(2, textOfChildren(Mytems.EARTH, text(" All Wardrobe Items", LIGHT_PURPLE)), Mytems.EARTH),
    //UNLOCKED(2, textOfChildren(Mytems.CHECKED_CHECKBOX, text(" Your Wardrobe Items", GOLD)), Mytems.CHECKED_CHECKBOX),
    HAT(3, text("Hats", AQUA), Mytems.PIRATE_HAT),
    HANDHELD(4, text("Handheld", AQUA), Mytems.CUPID_WINGS),
    FLAG(5, text("Flags"), Mytems.PRIDE_FLAG),
    MOUNT(6, text("Mounts", AQUA), Mytems.SANTA_SLED),
    COMPANION(7, text("Companions", GREEN), Mytems.HEART),
    ;

    public final int guiIndex;
    public final Component displayName;
    public final Mytems mytems;

    public ItemStack createIcon() {
        return mytems.createIcon(List.of(displayName));
    }

    public List<WardrobeItem> getItems() {
        List<WardrobeItem> result = new ArrayList<>();
        for (WardrobeItem it : WardrobeItem.all()) {
            if (it.getCategory() == this) result.add(it);
        }
        return result;
    }
}
