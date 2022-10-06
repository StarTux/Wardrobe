package com.cavetale.wardrobe;

import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/*
 * One package of items purchasable by player.
 */
public enum Package {
    WHITE_BUNNY(Component.text("White Bunny", NamedTextColor.WHITE),
                Hat.WHITE_BUNNY_EARS, Costume.WHITE_BUNNY),
    RED_LIGHTSABER(Component.text("Red Laser Sword", NamedTextColor.RED),
                   Handheld.RED_LIGHTSABER),
    BLUE_LIGHTSABER(Component.text("Blue Laser Sword", NamedTextColor.BLUE),
                    Handheld.BLUE_LIGHTSABER),
    PIRATE_HAT(Component.text("Pirate Hat", NamedTextColor.GRAY),
               Hat.PIRATE_HAT,
               Costume.PIRATE_PARROT),
    COWBOY_HAT(Component.text("Cowboy Hat", NamedTextColor.GOLD),
               Hat.COWBOY_HAT,
               Costume.GREEN_COWBOY_FROG,
               Costume.ORANGE_COWBOY_FROG),
    ANGEL(Component.text("Angel", NamedTextColor.YELLOW),
          Hat.ANGEL_HALO),
    DEVIL(Component.text("Devil", NamedTextColor.RED),
          Hat.DEVIL_HORNS),
    FIREMAN_HELMET(Hat.FIREMAN_HELMET),
    PLAGUE_DOCTOR(Hat.PLAGUE_DOCTOR,
                  Hat.PLAGUE_DOCTOR_2,
                  Costume.PLAGUE_DOCTOR),
    PUMPKIN(Hat.PUMPKIN_STUB),
    SANTA(Hat.SANTA_HAT,
          Costume.SANTA_POLAR_BEAR),
    STRAW_HAT(Hat.STRAW_HAT),
    WITCH_HAT(Component.text("Witch Hat", NamedTextColor.LIGHT_PURPLE),
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
    CAT_EARS(Component.text("Cat Ears", NamedTextColor.WHITE),
             Hat.BLACK_CAT_EARS,
             Hat.CYAN_CAT_EARS,
             Hat.LIGHT_BLUE_CAT_EARS,
             Hat.LIME_CAT_EARS,
             Hat.ORANGE_CAT_EARS,
             Hat.PINK_CAT_EARS,
             Hat.RED_CAT_EARS,
             Hat.WHITE_CAT_EARS,
             Costume.CAVETALE),
    SUNGLASSES(Component.text("Sunglasses", NamedTextColor.LIGHT_PURPLE),
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
    TOP_HAT(Component.text("Top Hat", NamedTextColor.GRAY),
            Hat.TOP_HAT,
            Costume.TUXEDO_CAT),
    ELF_HAT(Component.text("Elf Hat", NamedTextColor.RED),
            Hat.ELF_HAT,
            Costume.ELF);

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
        this.displayName = wardrobeItems[0].getDisplayName();
        this.wardrobeItems = List.of(wardrobeItems);
    }

    public static Package of(String in) {
        try {
            return Package.valueOf(in.toUpperCase());
        } catch (IllegalArgumentException iae) {
            return null;
        }
    }
}
