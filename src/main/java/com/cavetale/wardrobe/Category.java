package com.cavetale.wardrobe;

import com.cavetale.mytems.Mytems;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;

/*
 * All the categories displayed in the menu.
 */
public enum Category {
    ALL(Component.text("All", NamedTextColor.LIGHT_PURPLE), Mytems.EARTH),
    UNLOCKED(Component.text("Unlocked", NamedTextColor.GOLD), Mytems.GOLDEN_KEY),
    SWORDS(Component.text("Swords", NamedTextColor.RED),
           Package.RED_LIGHTSABER,
           Package.BLUE_LIGHTSABER),
    HATS(Component.text("Hats", NamedTextColor.GRAY),
         Package.TOP_HAT,
         Package.WITCH_HAT,
         Package.SANTA,
         Package.ELF_HAT,
         Package.PIRATE_HAT,
         Package.COWBOY_HAT,
         Package.FIREFIGHTER_HELMET,
         Package.STRAW_HAT),
    MASKS(Component.text("Masks", NamedTextColor.GOLD),
             Package.KOBOLD,
             Package.PLAGUE_DOCTOR),
    ACCESSORIES(Component.text("Accessories", BlockColor.PINK.textColor),
                Package.SUNGLASSES,
                Package.CAT_EARS,
                Package.WHITE_BUNNY,
                Package.PUMPKIN,
                Package.ANGEL,
                Package.DEVIL),
    ;

    public final Component displayName;
    public final List<Package> packages;
    public final List<WardrobeItem> wardrobeItems;
    public final ItemStack icon;

    Category(final Component displayName, final Package... packages) {
        this.displayName = displayName;
        this.packages = List.of(packages);
        List<WardrobeItem> items = new ArrayList<>();
        for (Package pack : packages) {
            items.addAll(pack.wardrobeItems);
        }
        this.wardrobeItems = List.copyOf(items);
        this.icon = wardrobeItems.get(0).toMenuItem();
    }

    Category(final Component displayName, final Mytems mytems) {
        this.displayName = displayName;
        this.packages = List.of(Package.values());
        this.wardrobeItems = WardrobeItem.all();
        this.icon = mytems.createIcon(List.of(displayName));
    }
}
