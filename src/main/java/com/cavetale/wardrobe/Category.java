package com.cavetale.wardrobe;

import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/*
 * One of the categories displayed in the menu.
 */
public enum Category {
    WHITE_BUNNY(Component.text("Bunny", NamedTextColor.WHITE), Package.WHITE_BUNNY),
    LASER_SWORDS(Component.text("Laser Swords", NamedTextColor.RED),
                 Package.RED_LIGHTSABER, Package.BLUE_LIGHTSABER),
    HATS(Component.text("Hats", NamedTextColor.GRAY),
         Package.PIRATE_HAT,
         Package.COWBOY_HAT,
         Package.ANGEL_HALO),
    CAT_EARS(Component.text("Cat Ears", NamedTextColor.WHITE),
             Package.CAT_EARS),
    SUNGLASSES(Component.text("Sunglasses", NamedTextColor.LIGHT_PURPLE),
               Package.SUNGLASSES);

    public final Component displayName;
    public final List<Package> packages;
    public final List<WardrobeItem> wardrobeItems;

    Category(final Component displayName, final Package... packages) {
        this.displayName = displayName;
        this.packages = List.of(packages);
        List<WardrobeItem> items = new ArrayList<>();
        for (Package pack : packages) {
            items.addAll(pack.wardrobeItems);
        }
        this.wardrobeItems = List.copyOf(items);
    }
}
