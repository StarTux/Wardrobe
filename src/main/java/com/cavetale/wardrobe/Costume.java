package com.cavetale.wardrobe;

import com.cavetale.wardrobe.util.Items;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

@Getter @SuppressWarnings("LineLength")
public enum Costume implements WardrobeItem {
    WHITE_BUNNY(Component.text("White Bunny Costume", NamedTextColor.WHITE),
                UUID.fromString("78749587-49cd-470b-b12a-666cde5d"),
                "ewogICJ0aW1lc3RhbXAiIDogMTYxNzQ3NTc1ODI1NSwKICAicHJvZmlsZUlkIiA6ICI3ODc0OTU4NzQ5Y2Q0NzBiYjEyYTY2NmNkZTVkODVmMCIsCiAgInByb2ZpbGVOYW1lIiA6ICJNYW50cmlYeEQiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzQyNzllOGIyYmExZTFhNDI4OTkwMzBjMjBhOThhYjQzNWQyOWRmNzVhNDZmOWM1ZTVlMGQ3MzkxOGQzOGIzNyIKICAgIH0KICB9Cn0=",
                "BpJRS19BaXiCVjv3A8jGagwL5yXnHiaKtdxvG5IDlvEEmZuyUD65c4Qrk2vcEubc22Tyzz2GEIrf7xZgDNBZaTtWtENaBXd7/gLTUyLyfCtYva0wd2bBbCY3K2MQfx6CjIjXZBTvz0z9nON7hACiADodX9Y8tZyoFsGTxzfAKtItPdkEQamzIqYjp2o/VPF1394NweAqUOfIjEcSJggBcgsjHwxqi9XVbQyO9dc4Dw8M62hOeb4DhJANZJlLzN8PSwmR8FWmgfHxY4SbkkZrw/wWTQ5+G7QI7vgBf4DRaYtqfyFUHrxlZ8enzyjkiLWhCfPhDJu1M+8uaGwq+w75HHmR6vUAi5xO+vipznr9rbwGXP0Mckq4CUWGFY1Dc3M4T/+mCXsavZyetu1QTpEUvSDoS92GFw+FhqC09fc70ctXrWVzA48KB6uYwYwnX1Nu9CBd807Rc843W4cSyZrlDrySPvlfT815r1kSXEj/E4wX2l5pkz91/kRey0BfPtJSajqyfNxBApFqnhzPn9G+zkxsNz8VsUYLRr6ZUUW7yK/Az5U/oyBFoCfLKuBTem5nV51C6Eq8LBGUOlNbAVQzl2zmhLsv2XdTkGx9p5Pgka+efG4RUufMShpy286YipAD7WlXpiXJvbJCYHK3Y99jiYs0diAnmBZdKt5+HMGOjd0="),
    PLAGUE_DOCTOR(Component.text("Plague Doctor", NamedTextColor.DARK_GRAY),
                  UUID.fromString("3b80985a-e886-4efe-b078-862fd94a15d9"),
                  "ewogICJ0aW1lc3RhbXAiIDogMTYzNTg1ODMwNDQzNiwKICAicHJvZmlsZUlkIiA6ICIzYjgwOTg1YWU4ODY0ZWZlYjA3ODg2MmZkOTRhMTVkOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJLaWVyYW5fVmF4aWxpYW4iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJjNzNjMDQwYWIzNWE1N2E1OTkzZGVmODUwNTczNTFjMzhlNmU2OGZjZTgwMTZlMGQ4ZjYzMmUwNDY4Mjk1MiIKICAgIH0KICB9Cn0=",
                  "ByS6VQEvitB56JUPw3umnBJ/wXGcpAmrehjPx/XmEgbWUeIYqQsmAmVgf7/jS3GeG7StQv1bKPVh1fsmkNOhQMTvmWd4a9Wq70DzKE7FcSv3AawMq/JprcFzQYnHKWVvoyZ5je/6dIEshBykxFdEjABxRyBzaSUAWAU9vD4Tg7VvNehzsVMEvDmMljjl1SBozak8YeHi9Tm80CkSZIybubhjKDp1mOum5mTf0djK5sTkdmi/c/5gPBea2oBHt8DvYry+vTLBXLai8h22NAUBzK2VdhRzEu+mPh2u2eb0fD51+E0XoAnYiYeRWLiGXu3oioYDKMtYeN5rRtJ4YBL2UEMtS/OQ7vAsRGrIB1XK/x6orb7y5OPCo/EiMMBytN1400H8bYqD/txFZ/I/FgdjYzsRiVM+r/V8PVED8Uh1yfK0qkbZHDpFeY74mAUFjXrP3i6VZGxkW+jEAxI0lCfZim3B6+B8x6Kfs9qUFTzgrhHvXm02gYwZw0ErSEr8OC2kpkgObalWI0OAAr4ZZSCvSeVKDe2A65ScqvZ8U75/8CZBfKGW6z7y/yb6YKbY/dVDXRerUj195K5sy+YQdRsNxvKj612d4GEDM1jYjia/g3BAHcrMUh6Jkz7Of6EDDuxtlgVICmN1620bN+MNyVVkB+iZHk27P/VD3GxLfi7uFqg="),
    GREEN_COWBOY_FROG(Component.text("Green Cowboy Frog", NamedTextColor.DARK_GREEN),
                      UUID.fromString("c56e2242-cbef-41a6-87e3-260dc0cf9361"),
                      "ewogICJ0aW1lc3RhbXAiIDogMTYzNTYzODcyMzY1MCwKICAicHJvZmlsZUlkIiA6ICJjNTZlMjI0MmNiZWY0MWE2ODdlMzI2MGRjMGNmOTM2MSIsCiAgInByb2ZpbGVOYW1lIiA6ICJMSlI3MzEwMCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xZGQwZTk3YTJjNGZhYTU0OGZhNTYyOGUzOTUwMTk0ODM2ZmQ2ZDEyYWRjODFhNTU2YzFlMGUyMDUyYmViZTcxIgogICAgfQogIH0KfQ==",
                      "sLQIdqOqoUjmvC4uzQ1wwUBty1rd5pOCs6YonbbW3A7KMeFUqt8XQP8QI3XwnXlSN7M4pUyxxt0RAfB5k7LEavl4HvIEg7qi08UqsiX+/Hyv6brwnl/d+3333NS035gY9j5jgkJuWuqkfupbt1eDEu5XPO6AGmokeCcx6sGQUzj66L7SHPY4lmfRPWlobN73K2tFZ/9GmtatW46GzlG/l7PsQEZlXXDWDAjEmf51OdIK06xvxzF/tRISwapNv/cGI1OVCxhn4s5NuH/6ILWJOb7R2zuIpp5xIGG4egldZX1gIzMQzYw4sSfLjI4GOj2PEiO4ml08lKBQtsQBtBk4l9q9Baop/Q+RFKhVk8MJVYlOhvkTIRgpF090+n7a1W6m89XHVbWZSOgDYumV0a4y9QNXryW/n9/47IqLem6HUP1pBxEGGNwADJ4hrOW2Ktwq/AM9y8vWx+vRdwko89LMRRY20P14zadc5wamZAyvLDLLw+0qEwwPx+CVNH1B5gKN2X8Nx6XvTyySK8JczJiiY2aLaEZYC0J7V6J9zumpGdOY8YUbvpAe1dVSzA7lJCt/RzS8o7Nk7OMHHna73+ha7Yg+UeRl7YdQmJ09VUJPBCaADmImZdY33NPq7sUySM36eZNyOgK86Beq9bns1WSTzT9t9jsj/eJuVDi/6TyYwfA="),
    ORANGE_COWBOY_FROG(Component.text("Orange Cowboy Frog", NamedTextColor.GOLD),
                       UUID.fromString("f25911b9-6dd5-422a-a707-3b90f8b81523"),
                       "ewogICJ0aW1lc3RhbXAiIDogMTYzNTYzODU4NTMxMiwKICAicHJvZmlsZUlkIiA6ICJmMjU5MTFiOTZkZDU0MjJhYTcwNzNiOTBmOGI4MTUyMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJmYXJsb3VjaDEwMCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9iNWFkYTVjN2RiMmI1YjhjZjU4NDc1MTQyYzRmYmU0ZmUxOWY4OTdkMzUxNzRhZTZhMzk2ZmFhOWY0MmYzNWZjIgogICAgfQogIH0KfQ==",
                       "R5Hx/vF7zknpufnWgG/ZPMYJBcuLwRLHPOIyrnDX0nU8eBK5cXXOSU7qXr35Ae1VD7ajr9zolVcddWOxcxwFWLLs5iuVNv3NCADA2x3uslbBKjZJtsE2sPonG5Lhe2DOSdPuFtLdhiNo1q98caDeO/guundP08NWeeg+3/CFiBzHtMSEECz/JjBvFP5rbE1667Z1YrKkumAQrByLmMJisc/z+l3lF2yS1qTDhVAAsvRaiWxdxmysELyzQ3IVW06dlptE8cAynOjPfAC+TEu9Th+fWv5DIEEcEe7dB/ovTYoVj0Pc3hUM+HjuK4l+0sUIAfI0Fc0oVJCCPXgXRSQpQrCqF80CFKPrwgckqr0dhbJ8/wri7U+F8MCxr3xQbnDWqolztnjBRmuhbnNtHhk1SQ8bH1jVam0Vc0wr+wPsKN0tV6MtI10yaNdHC3iSSmZoc2eMuDzTtulbeO5JJNgUC03cuUs55YKhnIjq81vDYcHOvJkg6YEkXZWt1xDj7Sp4ba0iGz2YcHEtplgu5Op8Tyu3c1M/rT2VVgWY7rt4oXwEjE95WA9v55+ev2UfmYjOL94nOoWw90vWbjvrBn3cYHS/xrhpEtKQVHK7eZhW9Qm+EXkMxeqssd8s2JRloYsU4F9w7lYAI+sCePDiUFIVY6gzvHAHLKJPsS6/PyoID4s="),
    PIRATE_PARROT(Component.text("Pirate Parrot", NamedTextColor.GOLD),
                  UUID.fromString("3b80985a-e886-4efe-b078-862fd94a15d9"),
                  "ewogICJ0aW1lc3RhbXAiIDogMTYzNTk2ODQxNTI3MywKICAicHJvZmlsZUlkIiA6ICIzYjgwOTg1YWU4ODY0ZWZlYjA3ODg2MmZkOTRhMTVkOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJLaWVyYW5fVmF4aWxpYW4iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWQ4NjI4ZGYxY2RlYTk2MzAwYjQ2NmFiYThiM2I5NjI0NTVmZmRhYzE5YTZhNTE4ZWFiZTg1ZWUzYzY5Y2U0MCIKICAgIH0KICB9Cn0=",
                  "PlBDZ2LSu7uhA46SA+RUfx7XiRbBBJZeil7OrUDXO7yfnEKuY4OwEfuZfs3Ehe58QQbnD33jdP+nPsknyoGd+rcGKUs8JYmPVIHsauPG2/ePJtQ119HeMmuIy8SJMFhMDa/wsCCdeMnWPG1n8S/kUj24+mMS2Mxkb+qRWjUoxE11q4RjUJPcxqFfQrbd1IEkQQA73YYFoCbOS96p76xh/dv9740erGqV8f3r0OCwJrnmoye6vdXYOnHCFsQj98GpTOdN0plMPf7vyMQWOtTdpRtsQsSIOdnXUogqpMZkEUGxGEF00I1bgZtcyIR4ZgqUhItwuvNjJvXswgDX1ecei5aa80125JrXDEmb8YYTGqNmTvh1AKEkwnryHoG1bdQj8vkYnBIG8CJ10gHS1AKvhh1bSOPtZ+PMGpniVs3zL8Nq84DxVFhVlWg0uRV4hiDoQpVVIpfLJKOr/GmsNJF0dKkvp7Fk0U5f9WQ+sAG2UE27bHnT9ovwWOFNtjobpuUdHBqkF3jiLAYiBb/T0BsVveZJ5iUHVzvVwWXjuVvrQvVNVJUkVIL9hkBLiWx+8K1X5nCPRxujph4f0ZR0qc7XttIJuHg0etRlLM6cB3ioAPalUbE/SQTXh1AaOFtDcdbom47tBOheJwVXgOUsPuA6zbOr51T2wd2+7F8jvNLfQtA=");

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

    @Override
    public ItemStack toMenuItem() {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        itemStack.editMeta(m -> {
                SkullMeta meta = (SkullMeta) m;
                Items.text(meta, List.of(displayName,
                                         Component.text("Player Skin", NamedTextColor.DARK_PURPLE),
                                         Component.text("Click to equip", TextColor.color(0xFFFF00))));
                meta.setPlayerProfile(toPlayerProfile());
            });
        return itemStack;
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event) {
        if (event.getClick() != ClickType.LEFT) return;
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1.0f, 1.0f);
        if (this == remove(player)) {
            player.sendMessage(Component.text("Costume removed!").color(WardrobeCommand.COLOR));
            return;
        }
        wear(player);
        player.sendMessage(Component.text("Costume equipped: ").color(WardrobeCommand.COLOR)
                           .append(displayName));
    }
}
