package com.cavetale.wardrobe;

import com.cavetale.mytems.Mytems;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;

/*
 * One of the categories displayed in the menu.
 */
public enum Category {
    ALL(Component.text("All", NamedTextColor.LIGHT_PURPLE), Mytems.QUESTION_MARK),
    UNLOCKED(Component.text("Unlocked", NamedTextColor.GOLD), Mytems.EXCLAMATION_MARK),
    WHITE_BUNNY(Component.text("Bunny", NamedTextColor.WHITE), Package.WHITE_BUNNY),
    LASER_SWORDS(Component.text("Laser Swords", NamedTextColor.RED),
                 Package.RED_LIGHTSABER, Package.BLUE_LIGHTSABER),
    COSTUMES(Component.text("Costumes", NamedTextColor.GRAY),
             Package.PIRATE_HAT,
             Package.COWBOY_HAT,
             Package.ANGEL,
             Package.DEVIL,
             Package.FIREMAN_HELMET,
             Package.PLAGUE_DOCTOR,
             Package.PUMPKIN,
             Package.SANTA,
             Package.STRAW_HAT,
             Package.WITCH_HAT),
    CAT_EARS(Component.text("Cat Ears", NamedTextColor.WHITE),
             Package.CAT_EARS),
    SUNGLASSES(Component.text("Sunglasses", NamedTextColor.LIGHT_PURPLE),
               Package.SUNGLASSES);

    public final Component displayName;
    //public final List<Package> packages;
    public final List<WardrobeItem> wardrobeItems;
    public final ItemStack icon;

    Category(final Component displayName, final Package... packages) {
        this.displayName = displayName;
        //this.packages = List.of(packages);
        List<WardrobeItem> items = new ArrayList<>();
        for (Package pack : packages) {
            items.addAll(pack.wardrobeItems);
        }
        this.wardrobeItems = List.copyOf(items);
        this.icon = wardrobeItems.get(0).toMenuItem();
    }

    Category(final Component displayName, final Mytems mytems) {
        this.displayName = displayName;
        this.wardrobeItems = WardrobeItem.all();
        this.icon = mytems.createIcon(List.of(displayName));
    }
}
