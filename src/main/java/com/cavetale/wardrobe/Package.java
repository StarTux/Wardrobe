
package com.cavetale.wardrobe;

/*
 * One package of items purchasable by player.
 */
public enum Package {
    WHITE_BUNNY(Hat.WHITE_BUNNY_EARS, Costume.WHITE_BUNNY),
    RED_LIGHTSABER(Handheld.RED_LIGHTSABER),
    BLUE_LIGHTSABER(Handheld.BLUE_LIGHTSABER);

    public final Hat hat;
    public final Costume costume;
    public final Handheld handheld;

    Package(final Hat hat, final Costume costume) {
        this.hat = hat;
        this.costume = costume;
        this.handheld = null;
    }

    Package(final Handheld handheld) {
        this.hat = null;
        this.costume = null;
        this.handheld = handheld;
    }

    public static Package of(String in) {
        try {
            return Package.valueOf(in.toUpperCase());
        } catch (IllegalArgumentException iae) {
            return null;
        }
    }
}
