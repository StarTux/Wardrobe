package com.cavetale.wardrobe;

/*
 * One package of items purchasable by player.
 */
public enum Package {
    WHITE_BUNNY(Hat.WHITE_BUNNY_EARS, Costume.WHITE_BUNNY);

    public final Hat hat;
    public final Costume costume;

    Package(final Hat hat, final Costume costume) {
        this.hat = hat;
        this.costume = costume;
    }

    public static Package of(String in) {
        try {
            return Package.valueOf(in.toUpperCase());
        } catch (IllegalArgumentException iae) {
            return null;
        }
    }
}
