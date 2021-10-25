package com.cavetale.wardrobe;

import net.kyori.adventure.text.format.TextColor;

public enum BlockColor {
    BLACK(0x1D1D21),
    RED(0xB02E26),
    GREEN(0x5E7C16),
    BROWN(0x835432),
    BLUE(0x3C44AA),
    PURPLE(0x8932B8),
    CYAN(0x169C9C),
    LIGHT_GRAY(0x9D9D97),
    GRAY(0x474F52),
    PINK(0xF38BAA),
    LIME(0x80C71F),
    YELLOW(0xFED83D),
    LIGHT_BLUE(0x3AB3DA),
    MAGENTA(0xC74EBD),
    ORANGE(0xF9801D),
    WHITE(0xF9FFFE);

    public final TextColor textColor;

    BlockColor(final int hex) {
        this.textColor = TextColor.color(hex);
    }
}
