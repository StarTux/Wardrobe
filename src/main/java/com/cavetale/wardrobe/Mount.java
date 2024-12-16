package com.cavetale.wardrobe;

import com.cavetale.mytems.Mytems;
import com.cavetale.wardrobe.mount.ArmorStandMountAdapter;
import com.cavetale.wardrobe.mount.DragonMountAdapter;
import com.cavetale.wardrobe.mount.MountAdapter;
import com.cavetale.wardrobe.mount.MountResult;
import com.cavetale.wardrobe.mount.Ride;
import java.util.List;
import java.util.function.Supplier;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import static com.cavetale.mytems.util.Items.tooltip;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextColor.color;

@Getter
public enum Mount implements WardrobeItem {
    SANTA_SLED(text("Santa's Sleigh", RED),
               () -> new ArmorStandMountAdapter(Mytems.SANTA_SLED, 0.5),
               Mytems.SANTA_SLED::createIcon),
    DRAGON(text("Dragon", LIGHT_PURPLE), DragonMountAdapter::new, () -> new ItemStack(Material.DRAGON_HEAD)),
    ;

    public final Component displayName;
    private final MountAdapter adapter;
    private final Supplier<ItemStack> iconSupplier;

    Mount(final Component displayName, final Supplier<MountAdapter> adapterCtor, final Supplier<ItemStack> iconSupplier) {
        this.displayName = displayName;
        this.adapter = adapterCtor.get();
        this.iconSupplier = iconSupplier;
    }

    @Override
    public Category getCategory() {
        return Category.MOUNT;
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
        Ride ride = Ride.ofPlayer(player);
        if (ride == null) return null;
        return ride.getMount();
    }

    @Override
    public ItemStack toMenuItem() {
        ItemStack itemStack = iconSupplier.get();
        itemStack.editMeta(meta -> {
                meta.addItemFlags(ItemFlag.values());
                tooltip(meta, List.of(displayName,
                                      text("Mount", DARK_PURPLE),
                                      text("Click to mount", color(0xFFFF00))));
            });
        return itemStack;
    }

    @Override
    public List<Component> getMenuTooltip() {
        return List.of(getDisplayName(),
                       text("Mount", DARK_GRAY),
                       empty(),
                       textOfChildren(Mytems.MOUSE_LEFT, text(" Ride", GRAY)));
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

    @Override
    public boolean isWearing(Player player) {
        return of(player) == this;
    }

    @Override
    public int getIndex() {
        return ordinal();
    }
}
