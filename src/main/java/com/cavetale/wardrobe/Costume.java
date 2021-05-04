package com.cavetale.wardrobe;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public enum Costume {
    WHITE_BUNNY(Component.text("White Bunny Costume", TextColor.color(0xFFFFFF)),
                UUID.fromString("78749587-49cd-470b-b12a-666cde5d"),
                "ewogICJ0aW1lc3RhbXAiIDogMTYxNzQ3NTc1ODI1NSwKICAicHJvZmlsZUlkIiA6ICI3ODc0OTU4NzQ5Y2Q0NzBiYjEyYTY2NmNkZTVkODVmMCIsCiAgInByb2ZpbGVOYW1lIiA6ICJNYW50cmlYeEQiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzQyNzllOGIyYmExZTFhNDI4OTkwMzBjMjBhOThhYjQzNWQyOWRmNzVhNDZmOWM1ZTVlMGQ3MzkxOGQzOGIzNyIKICAgIH0KICB9Cn0=",
                "BpJRS19BaXiCVjv3A8jGagwL5yXnHiaKtdxvG5IDlvEEmZuyUD65c4Qrk2vcEubc22Tyzz2GEIrf7xZgDNBZaTtWtENaBXd7/gLTUyLyfCtYva0wd2bBbCY3K2MQfx6CjIjXZBTvz0z9nON7hACiADodX9Y8tZyoFsGTxzfAKtItPdkEQamzIqYjp2o/VPF1394NweAqUOfIjEcSJggBcgsjHwxqi9XVbQyO9dc4Dw8M62hOeb4DhJANZJlLzN8PSwmR8FWmgfHxY4SbkkZrw/wWTQ5+G7QI7vgBf4DRaYtqfyFUHrxlZ8enzyjkiLWhCfPhDJu1M+8uaGwq+w75HHmR6vUAi5xO+vipznr9rbwGXP0Mckq4CUWGFY1Dc3M4T/+mCXsavZyetu1QTpEUvSDoS92GFw+FhqC09fc70ctXrWVzA48KB6uYwYwnX1Nu9CBd807Rc843W4cSyZrlDrySPvlfT815r1kSXEj/E4wX2l5pkz91/kRey0BfPtJSajqyfNxBApFqnhzPn9G+zkxsNz8VsUYLRr6ZUUW7yK/Az5U/oyBFoCfLKuBTem5nV51C6Eq8LBGUOlNbAVQzl2zmhLsv2XdTkGx9p5Pgka+efG4RUufMShpy286YipAD7WlXpiXJvbJCYHK3Y99jiYs0diAnmBZdKt5+HMGOjd0=");

    public final Component displayName;
    public final UUID uuid;
    public final String texture;
    public final String signature;
    static Map<UUID, PlayerProfile> backups = new HashMap<>();
    static Map<UUID, Costume> worn = new HashMap<>();

    Costume(final Component displayName, final UUID uuid, final String texture, final String signature) {
        this.displayName = displayName;
        this.uuid = uuid;
        this.texture = texture;
        this.signature = signature;
    }

    public void wear(Player player) {
        if (!backups.containsKey(player.getUniqueId())) {
            backups.put(player.getUniqueId(), player.getPlayerProfile());
        }
        PlayerProfile profile = player.getPlayerProfile();
        profile.setProperty(new ProfileProperty("textures", texture, signature));
        player.setPlayerProfile(profile);
        worn.put(player.getUniqueId(), this);
    }

    public static Costume remove(Player player) {
        PlayerProfile backup = backups.remove(player.getUniqueId());
        if (backup != null) {
            player.setPlayerProfile(backup);
        }
        return worn.remove(player.getUniqueId());
    }

    public PlayerProfile toPlayerProfile() {
        PlayerProfile profile = Bukkit.createProfile(uuid);
        profile.setProperty(new ProfileProperty("textures", texture, signature));
        return profile;
    }

    public ItemStack toPlayerHead() {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.displayName(displayName.decoration(TextDecoration.ITALIC, false));
        meta.lore(Arrays.asList(Component.text()
                                .append(Component.text("Click to equip", TextColor.color(0xFFFF00)))
                                .decoration(TextDecoration.ITALIC, false).build()));
        meta.setPlayerProfile(toPlayerProfile());
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
