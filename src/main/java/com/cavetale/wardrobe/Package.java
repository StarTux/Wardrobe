package com.cavetale.wardrobe;

import com.cavetale.wardrobe.companion.CompanionType;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

/*
 * One package of items purchasable by player.
 */
public enum Package {
    WHITE_BUNNY(Hat.WHITE_BUNNY_EARS, CompanionType.EASTER_EGGS, Costume.WHITE_BUNNY),
    RED_LIGHTSABER(Handheld.RED_LIGHTSABER),
    BLUE_LIGHTSABER(Handheld.BLUE_LIGHTSABER),
    PIRATE_HAT(Hat.PIRATE_HAT,
               Costume.PIRATE_PARROT),
    COWBOY_HAT(Hat.COWBOY_HAT,
               Costume.GREEN_COWBOY_FROG,
               Costume.ORANGE_COWBOY_FROG),
    ANGEL(Hat.ANGEL_HALO),
    DEVIL(Hat.DEVIL_HORNS),
    CUPID_WINGS(Handheld.CUPID_WINGS, Hat.ANGEL_HALO),
    DRAGON_MOUNT(Mount.DRAGON),
    FIREFIGHTER_HELMET(Hat.FIREFIGHTER_HELMET),
    PLAGUE_DOCTOR(Hat.PLAGUE_DOCTOR,
                  Hat.PLAGUE_DOCTOR_2,
                  Costume.PLAGUE_DOCTOR),
    PUMPKIN(Hat.PUMPKIN_STUB),
    SANTA(Hat.SANTA_HAT,
          Costume.SANTA_POLAR_BEAR),
    CHRISTMAS_HAT(Hat.CHRISTMAS_HAT),
    SANTA_SLED(Mount.SANTA_SLED,
               Hat.SANTA_HAT,
               Hat.CHRISTMAS_HAT),
    STRAW_HAT(Hat.STRAW_HAT),
    WITCH_HAT(text("Witch Hat", LIGHT_PURPLE),
              Hat.PURPLE_WITCH_HAT,
              Hat.WHITE_WITCH_HAT,
              Hat.ORANGE_WITCH_HAT,
              Hat.MAGENTA_WITCH_HAT,
              Hat.LIGHT_BLUE_WITCH_HAT,
              Hat.YELLOW_WITCH_HAT,
              Hat.LIME_WITCH_HAT,
              Hat.PINK_WITCH_HAT,
              Hat.GRAY_WITCH_HAT,
              Hat.LIGHT_GRAY_WITCH_HAT,
              Hat.CYAN_WITCH_HAT,
              Hat.BLUE_WITCH_HAT,
              Hat.BROWN_WITCH_HAT,
              Hat.GREEN_WITCH_HAT,
              Hat.RED_WITCH_HAT,
              Hat.BLACK_WITCH_HAT,
              Costume.WITCH),
    CAT_EARS(text("Cat Ears", WHITE),
             Hat.BLACK_CAT_EARS,
             Hat.CYAN_CAT_EARS,
             Hat.LIGHT_BLUE_CAT_EARS,
             Hat.LIME_CAT_EARS,
             Hat.ORANGE_CAT_EARS,
             Hat.PINK_CAT_EARS,
             Hat.RED_CAT_EARS,
             Hat.WHITE_CAT_EARS,
             Costume.CAVETALE),
    SUNGLASSES(text("Sunglasses", LIGHT_PURPLE),
               Hat.BLACK_SUNGLASSES,
               Hat.RED_SUNGLASSES,
               Hat.GREEN_SUNGLASSES,
               Hat.BLUE_SUNGLASSES,
               Hat.PURPLE_SUNGLASSES,
               Hat.CYAN_SUNGLASSES,
               Hat.LIGHT_GRAY_SUNGLASSES,
               Hat.GRAY_SUNGLASSES,
               Hat.PINK_SUNGLASSES,
               Hat.LIME_SUNGLASSES,
               Hat.YELLOW_SUNGLASSES,
               Hat.LIGHT_BLUE_SUNGLASSES,
               Hat.MAGENTA_SUNGLASSES,
               Hat.ORANGE_SUNGLASSES,
               Hat.WHITE_SUNGLASSES,
               Costume.CAVETALE),
    TOP_HAT(Hat.TOP_HAT,
            Costume.TUXEDO_CAT),
    ELF_HAT(Hat.ELF_HAT,
            Costume.ELF),
    KOBOLD(Hat.KOBOLD_HEAD),
    GOLDEN_CROWN(Hat.GOLDEN_CROWN),
    BUTTERFLY_WINGS(Handheld.BUTTERFLY_WINGS),
    BUTTERFLY_COMPANIONS(CompanionType.BLUE_BUTTERFLY,
                         CompanionType.CYAN_BUTTERFLY,
                         CompanionType.GREEN_BUTTERFLY,
                         CompanionType.ORANGE_BUTTERFLY,
                         CompanionType.PINK_BUTTERFLY,
                         CompanionType.PURPLE_BUTTERFLY,
                         CompanionType.YELLOW_BUTTERFLY),
    ;

    public final Component displayName;
    public final List<WardrobeItem> wardrobeItems;

    Package(final Component displayName, final WardrobeItem... wardrobeItems) {
        this.displayName = displayName;
        List<WardrobeItem> wardrobeItemList = new ArrayList<>();
        for (WardrobeItem item : wardrobeItems) {
            if (item instanceof Costume) continue;
            wardrobeItemList.add(item);
        }
        this.wardrobeItems = List.copyOf(wardrobeItemList);
    }

    Package(final WardrobeItem... wardrobeItems) {
        this(wardrobeItems[0].getDisplayName(), wardrobeItems);
    }

    public static Package of(String in) {
        try {
            return Package.valueOf(in.toUpperCase());
        } catch (IllegalArgumentException iae) {
            return null;
        }
    }
}
