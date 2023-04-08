package com.cavetale.wardrobe.emote;

import java.util.HashMap;
import java.util.Map;

/**
 * Global emote cache.
 */
public final class Emotes {
    private final Map<String, Emote> emotes = new HashMap<>();

    public Emote getEmote(String name) {
        return emotes.get(name.toLowerCase());
    }

    public void addEmote(Emote emote) {
        emotes.put(emote.name.toLowerCase(), emote);
    }
}
