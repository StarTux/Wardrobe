package com.cavetale.wardrobe;

import com.cavetale.mytems.Mytems;
import com.cavetale.wardrobe.mount.ArmorStandMount;
import com.cavetale.wardrobe.mount.MountAdapter;
import com.cavetale.wardrobe.mount.MountResult;
import com.cavetale.wardrobe.util.Items;
import java.util.List;
import java.util.function.Supplier;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextColor.color;

@Getter
public enum Mount implements WardrobeItem {
    SANTA_SLED(text("Santa's Sleigh", RED), Mytems.SANTA_SLED, () -> new ArmorStandMount(0.5)),
    ;

    public final Component displayName;
    public final Mytems mytems;
    private final MountAdapter adapter;

    Mount(final Component displayName, final Mytems mytems, final Supplier<MountAdapter> adapterCtor) {
        this.displayName = displayName;
        this.mytems = mytems;
        this.adapter = adapterCtor.get();
    }

    public boolean mount(Player player) {
        remove(player);
        MountResult result = adapter.mount(player, this);
        if (result != MountResult.SUCCESS) {
            player.sendActionBar(text(result.message, RED));
            player.sendMessage(text(result.message, RED));
            return false;
        } else {
            player.sendMessage(textOfChildren(text("Mounted: ", WardrobeCommand.COLOR), displayName));
            player.playSound(player.getLocation(), Sound.ENTITY_HORSE_SADDLE, SoundCategory.MASTER, 1.0f, 1.125f);
            return true;
        }
    }

    public boolean unmount(Player player) {
        return adapter.unmount(player, this);
    }

    public static Mount remove(Player player) {
        Mount mount = of(player);
        if (mount == null) return null;
        mount.unmount(player);
        return mount;
    }

    public static Mount of(Player player) {
        if (player.isInsideVehicle() && player.getVehicle() instanceof ArmorStand armorStand) {
            Mytems mytems = Mytems.forItem(armorStand.getEquipment().getHelmet());
            if (mytems != null) {
                for (Mount mount : Mount.values()) {
                    if (mount.mytems == mytems && mount.adapter instanceof ArmorStandMount) {
                        return mount;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public ItemStack toMenuItem() {
        ItemStack itemStack = mytems.getMytem().createItemStack();
        itemStack.editMeta(meta -> {
                Items.text(meta, List.of(displayName,
                                         text("Mount", DARK_PURPLE),
                                         text("Click to mount", color(0xFFFF00))));
            });
        return itemStack;
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event) {
        if (event.getClick() != ClickType.LEFT) return;
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1.0f, 1.0f);
        if (this == remove(player)) {
            player.sendMessage(textOfChildren(text("Mount removed: ", WardrobeCommand.COLOR), displayName));
            return;
        }
        mount(player);
    }
}
