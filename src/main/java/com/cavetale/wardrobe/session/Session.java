package com.cavetale.wardrobe.session;

import com.cavetale.wardrobe.companion.Companion;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public final class Session {
    protected final UUID uuid;
    protected final String name;
    @Getter @Setter protected Companion companion;

    protected Session(final Player player) {
        this(player.getUniqueId(), player.getName());
    }

    protected void enable() {
    }

    protected void disable() {
        if (companion != null) {
            companion.stop();
            companion = null;
        }
    }
}
