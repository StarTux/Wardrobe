package com.cavetale.wardrobe.session;

import com.cavetale.wardrobe.companion.Companion;
import com.cavetale.wardrobe.companion.CompanionType;
import com.cavetale.wardrobe.sql.SQLEquipped;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import static com.cavetale.wardrobe.WardrobePlugin.database;

@RequiredArgsConstructor
public final class Session {
    protected final UUID uuid;
    protected final String name;
    @Getter @Setter protected Companion companion;
    protected boolean enabled;
    protected SQLEquipped equipped;

    protected Session(final Player player) {
        this(player.getUniqueId(), player.getName());
    }

    protected void enable() {
        enabled = true;
        database().find(SQLEquipped.class).eq("player", uuid).findUniqueAsync(this::onLoadRow);
    }

    protected void disable() {
        enabled = false;
        if (companion != null) {
            companion.stop();
            companion = null;
        }
    }

    private void onLoadRow(SQLEquipped row) {
        if (!enabled) return;
        if (row == null) {
            this.equipped = new SQLEquipped(uuid);
            database().insertAsync(equipped, null);
            return;
        }
        this.equipped = row;
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        if (equipped.getCompanion() != null) {
            CompanionType type = CompanionType.of(equipped.getCompanion());
            if (type != null) type.equip(this, player);
        }
    }

    public void saveCompanion() {
        if (equipped == null) return;
        equipped.setCompanion(companion != null ? companion.getType().name().toLowerCase() : null);
        database().updateAsync(equipped, null, "companion");
    }
}
