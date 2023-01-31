package com.cavetale.wardrobe;

import com.cavetale.mytems.Mytems;
import com.cavetale.wardrobe.util.Items;
import java.util.List;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@Getter
public enum Hat implements WardrobeItem {
    WHITE_BUNNY_EARS(Component.text("White Bunny Ears", NamedTextColor.WHITE), Mytems.WHITE_BUNNY_EARS),
    //
    PIRATE_HAT(Component.text("Pirate Hat", NamedTextColor.GRAY), Mytems.PIRATE_HAT),
    COWBOY_HAT(Component.text("Cowboy Hat", NamedTextColor.GOLD), Mytems.COWBOY_HAT),
    ANGEL_HALO(Component.text("Angel Halo", NamedTextColor.YELLOW), Mytems.ANGEL_HALO),
    GOLDEN_CROWN(Component.text("Golden Crown", NamedTextColor.GOLD), Mytems.GOLDEN_CROWN),
    DEVIL_HORNS(Component.text("Devil Horns", NamedTextColor.DARK_RED), Mytems.DEVIL_HORNS),
    ELF_HAT(Component.text("Elf Hat", NamedTextColor.RED), Mytems.ELF_HAT),
    FIREFIGHTER_HELMET(Component.text("Firefighter Helmet", NamedTextColor.RED), Mytems.FIREFIGHTER_HELMET),
    PLAGUE_DOCTOR(Component.text("Plague Doctor", NamedTextColor.DARK_GRAY), Mytems.PLAGUE_DOCTOR),
    PLAGUE_DOCTOR_2(Component.text("Plague Doctor II", NamedTextColor.DARK_GRAY), Mytems.PLAGUE_DOCTOR_2),
    PUMPKIN_STUB(Component.text("Pumpkin Stub", NamedTextColor.GOLD), Mytems.PUMPKIN_STUB),
    SANTA_HAT(Component.text("Santa Hat", NamedTextColor.RED), Mytems.STOCKING_CAP),
    STRAW_HAT(Component.text("Straw Hat", NamedTextColor.YELLOW), Mytems.STRAW_HAT),
    CHRISTMAS_HAT(Component.text("Christmas Wizard Hat", NamedTextColor.RED), Mytems.CHRISTMAS_HAT),
    // Witch Hat
    WHITE_WITCH_HAT(Component.text("White Witch Hat", BlockColor.WHITE.textColor), Mytems.WHITE_WITCH_HAT),
    ORANGE_WITCH_HAT(Component.text("Orange Witch Hat", BlockColor.ORANGE.textColor), Mytems.ORANGE_WITCH_HAT),
    MAGENTA_WITCH_HAT(Component.text("Magenta Witch Hat", BlockColor.MAGENTA.textColor), Mytems.MAGENTA_WITCH_HAT),
    LIGHT_BLUE_WITCH_HAT(Component.text("Light Blue Witch Hat", BlockColor.LIGHT_BLUE.textColor), Mytems.LIGHT_BLUE_WITCH_HAT),
    YELLOW_WITCH_HAT(Component.text("Yellow Witch Hat", BlockColor.YELLOW.textColor), Mytems.YELLOW_WITCH_HAT),
    LIME_WITCH_HAT(Component.text("Lime Witch Hat", BlockColor.LIME.textColor), Mytems.LIME_WITCH_HAT),
    PINK_WITCH_HAT(Component.text("Pink Witch Hat", BlockColor.PINK.textColor), Mytems.PINK_WITCH_HAT),
    GRAY_WITCH_HAT(Component.text("Gray Witch Hat", BlockColor.GRAY.textColor), Mytems.GRAY_WITCH_HAT),
    LIGHT_GRAY_WITCH_HAT(Component.text("Light Gray Witch Hat", BlockColor.LIGHT_GRAY.textColor), Mytems.LIGHT_GRAY_WITCH_HAT),
    CYAN_WITCH_HAT(Component.text("Cyan Witch Hat", BlockColor.CYAN.textColor), Mytems.CYAN_WITCH_HAT),
    PURPLE_WITCH_HAT(Component.text("Purple Witch Hat", BlockColor.PURPLE.textColor), Mytems.PURPLE_WITCH_HAT),
    BLUE_WITCH_HAT(Component.text("Blue Witch Hat", BlockColor.BLUE.textColor), Mytems.BLUE_WITCH_HAT),
    BROWN_WITCH_HAT(Component.text("Brown Witch Hat", BlockColor.BROWN.textColor), Mytems.BROWN_WITCH_HAT),
    GREEN_WITCH_HAT(Component.text("Green Witch Hat", BlockColor.GREEN.textColor), Mytems.GREEN_WITCH_HAT),
    RED_WITCH_HAT(Component.text("Red Witch Hat", BlockColor.RED.textColor), Mytems.RED_WITCH_HAT),
    BLACK_WITCH_HAT(Component.text("Black Witch Hat", BlockColor.BLACK.textColor), Mytems.BLACK_WITCH_HAT),
    // Cat Ears
    BLACK_CAT_EARS(Component.text("Black Cat Ears", BlockColor.BLACK.textColor), Mytems.BLACK_CAT_EARS),
    CYAN_CAT_EARS(Component.text("Cyan Cat Ears", BlockColor.CYAN.textColor), Mytems.CYAN_CAT_EARS),
    LIGHT_BLUE_CAT_EARS(Component.text("Light Blue Cat Ears", BlockColor.LIGHT_BLUE.textColor), Mytems.LIGHT_BLUE_CAT_EARS),
    LIME_CAT_EARS(Component.text("Lime Cat Ears", BlockColor.LIME.textColor), Mytems.LIME_CAT_EARS),
    ORANGE_CAT_EARS(Component.text("Orange Cat Ears", BlockColor.ORANGE.textColor), Mytems.ORANGE_CAT_EARS),
    PINK_CAT_EARS(Component.text("Pink Cat Ears", BlockColor.PINK.textColor), Mytems.PINK_CAT_EARS),
    RED_CAT_EARS(Component.text("Red Cat Ears", BlockColor.RED.textColor), Mytems.RED_CAT_EARS),
    WHITE_CAT_EARS(Component.text("White Cat Ears", BlockColor.WHITE.textColor), Mytems.WHITE_CAT_EARS),
    // Sunglasses
    BLACK_SUNGLASSES(Component.text("Black Sunglasses", BlockColor.GRAY.textColor), Mytems.BLACK_SUNGLASSES),
    RED_SUNGLASSES(Component.text("Red Sunglasses", BlockColor.RED.textColor), Mytems.RED_SUNGLASSES),
    GREEN_SUNGLASSES(Component.text("Green Sunglasses", BlockColor.GREEN.textColor), Mytems.GREEN_SUNGLASSES),
    BLUE_SUNGLASSES(Component.text("Blue Sunglasses", BlockColor.BLUE.textColor), Mytems.BLUE_SUNGLASSES),
    PURPLE_SUNGLASSES(Component.text("Purple Sunglasses", BlockColor.PURPLE.textColor), Mytems.PURPLE_SUNGLASSES),
    CYAN_SUNGLASSES(Component.text("Cyan Sunglasses", BlockColor.CYAN.textColor), Mytems.CYAN_SUNGLASSES),
    LIGHT_GRAY_SUNGLASSES(Component.text("Light Gray Sunglasses", BlockColor.LIGHT_GRAY.textColor), Mytems.LIGHT_GRAY_SUNGLASSES),
    GRAY_SUNGLASSES(Component.text("Gray Sunglasses", BlockColor.GRAY.textColor), Mytems.GRAY_SUNGLASSES),
    PINK_SUNGLASSES(Component.text("Pink Sunglasses", BlockColor.PINK.textColor), Mytems.PINK_SUNGLASSES),
    LIME_SUNGLASSES(Component.text("Lime Sunglasses", BlockColor.LIME.textColor), Mytems.LIME_SUNGLASSES),
    YELLOW_SUNGLASSES(Component.text("Yellow Sunglasses", BlockColor.YELLOW.textColor), Mytems.YELLOW_SUNGLASSES),
    LIGHT_BLUE_SUNGLASSES(Component.text("Light Blue Sunglasses", BlockColor.LIGHT_BLUE.textColor), Mytems.LIGHT_BLUE_SUNGLASSES),
    MAGENTA_SUNGLASSES(Component.text("Magenta Sunglasses", BlockColor.MAGENTA.textColor), Mytems.MAGENTA_SUNGLASSES),
    ORANGE_SUNGLASSES(Component.text("Orange Sunglasses", BlockColor.ORANGE.textColor), Mytems.ORANGE_SUNGLASSES),
    WHITE_SUNGLASSES(Component.text("White Sunglasses", BlockColor.WHITE.textColor), Mytems.WHITE_SUNGLASSES),
    // Top Hat
    TOP_HAT(Component.text("Top Hat", NamedTextColor.GRAY), Mytems.TOP_HAT),
    // Kobold
    KOBOLD_HEAD(Component.text("Cobold Head", NamedTextColor.DARK_GREEN), Mytems.KOBOLD_HEAD),
    ;

    public final Component displayName;
    public final Mytems mytems;

    Hat(final Component displayName, final Mytems mytems) {
        this.displayName = displayName;
        this.mytems = mytems;
    }

    @Override
    public Category getCategory() {
        return Category.HAT;
    }

    /**
     * Attempt to empty the player helmet slot and put this hat
     * instead. It is recommended to call remove(Player) first.
     * @true if successful, false otherwise.
     */
    public boolean wear(Player player) {
        ItemStack itemStack = player.getInventory().getHelmet();
        if (itemStack != null && itemStack.getAmount() != 0) {
            player.getInventory().setHelmet(null);
            if (!player.getInventory().addItem(itemStack).isEmpty()) {
                // Can't add helmet item. Inventory must be full.
                player.getInventory().setHelmet(itemStack);
                return false;
            }
            itemStack = player.getInventory().getHelmet();
            if (itemStack != null && itemStack.getAmount() != 0) {
                // Item was added in helmet slot? Too bad.
                WardrobePlugin.getInstance().getLogger().warning("Item was added in helmet slot?");
                return false;
            }
        }
        // Helmet slot is empty now!
        itemStack = mytems.getMytem().createItemStack();
        player.getInventory().setHelmet(itemStack);
        return true;
    }

    /**
     * Remove any hat from the player.
     * @return the Hat that was removed or false
     */
    public static Hat remove(Player player) {
        Hat hat = of(player);
        if (hat == null) return null;
        player.getInventory().setHelmet(null);
        return hat;
    }

    /**
     * Get the hat that the player is currently wearing.
     * @return the worn Hat or null.
     */
    public static Hat of(Player player) {
        ItemStack itemStack = player.getInventory().getHelmet();
        if (itemStack == null || itemStack.getAmount() == 0) return null;
        Mytems mytems = Mytems.forItem(itemStack);
        if (mytems == null) return null;
        for (Hat hat : Hat.values()) {
            if (hat.mytems == mytems) return hat;
        }
        return null;
    }

    public static Hat of(ItemStack itemStack) {
        if (itemStack == null || itemStack.getAmount() == 0) return null;
        Mytems mytems = Mytems.forItem(itemStack);
        if (mytems == null) return null;
        for (Hat hat : Hat.values()) {
            if (hat.mytems == mytems) return hat;
        }
        return null;
    }

    @Override
    public ItemStack toMenuItem() {
        ItemStack itemStack = mytems.getMytem().createItemStack();
        itemStack.editMeta(meta -> {
                Items.text(meta, List.of(displayName,
                                         Component.text("Helmet Item", NamedTextColor.DARK_PURPLE),
                                         Component.text("Click to equip", TextColor.color(0xFFFF00))));
            });
        return itemStack;
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event) {
        if (event.getClick() != ClickType.LEFT) return;
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1.0f, 1.0f);
        if (this == remove(player)) {
            player.sendMessage(Component.text("Hat removed: ").color(WardrobeCommand.COLOR)
                               .append(displayName));
            return;
        }
        if (wear(player)) {
            player.sendMessage(Component.text("Hat equipped: ").color(WardrobeCommand.COLOR)
                               .append(displayName));
        } else {
            player.sendMessage(Component.text("Cannot equip ").color(TextColor.color(0xFF0000))
                               .append(displayName)
                               .append(Component.text(": Inventory is full!").color(TextColor.color(0xFF0000))));
        }
    }

    @Override
    public boolean isWearing(Player player) {
        return of(player) == this;
    }
}
