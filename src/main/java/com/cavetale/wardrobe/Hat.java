package com.cavetale.wardrobe;

import com.cavetale.mytems.Mytems;
import com.cavetale.wardrobe.util.Items;
import java.util.List;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.NamedTextColor.*;

@Getter
public enum Hat implements WardrobeItem {
    WHITE_BUNNY_EARS(text("White Bunny Ears", WHITE), Mytems.WHITE_BUNNY_EARS),
    //
    PIRATE_HAT(text("Pirate Hat", GRAY), Mytems.PIRATE_HAT),
    COWBOY_HAT(text("Cowboy Hat", GOLD), Mytems.COWBOY_HAT),
    ANGEL_HALO(text("Angel Halo", YELLOW), Mytems.ANGEL_HALO),
    GOLDEN_CROWN(text("Golden Crown", GOLD), Mytems.GOLDEN_CROWN),
    DEVIL_HORNS(text("Devil Horns", DARK_RED), Mytems.DEVIL_HORNS),
    ELF_HAT(text("Elf Hat", RED), Mytems.ELF_HAT),
    FIREFIGHTER_HELMET(text("Firefighter Helmet", RED), Mytems.FIREFIGHTER_HELMET),
    PLAGUE_DOCTOR(text("Plague Doctor", DARK_GRAY), Mytems.PLAGUE_DOCTOR),
    PLAGUE_DOCTOR_2(text("Plague Doctor II", DARK_GRAY), Mytems.PLAGUE_DOCTOR_2),
    PUMPKIN_STUB(text("Pumpkin Stub", GOLD), Mytems.PUMPKIN_STUB),
    SANTA_HAT(text("Santa Hat", RED), Mytems.STOCKING_CAP),
    STRAW_HAT(text("Straw Hat", YELLOW), Mytems.STRAW_HAT),
    CHRISTMAS_HAT(text("Christmas Wizard Hat", RED), Mytems.CHRISTMAS_HAT),
    // Witch Hat
    WHITE_WITCH_HAT(text("White Witch Hat", BlockColor.WHITE.textColor), Mytems.WHITE_WITCH_HAT),
    ORANGE_WITCH_HAT(text("Orange Witch Hat", BlockColor.ORANGE.textColor), Mytems.ORANGE_WITCH_HAT),
    MAGENTA_WITCH_HAT(text("Magenta Witch Hat", BlockColor.MAGENTA.textColor), Mytems.MAGENTA_WITCH_HAT),
    LIGHT_BLUE_WITCH_HAT(text("Light Blue Witch Hat", BlockColor.LIGHT_BLUE.textColor), Mytems.LIGHT_BLUE_WITCH_HAT),
    YELLOW_WITCH_HAT(text("Yellow Witch Hat", BlockColor.YELLOW.textColor), Mytems.YELLOW_WITCH_HAT),
    LIME_WITCH_HAT(text("Lime Witch Hat", BlockColor.LIME.textColor), Mytems.LIME_WITCH_HAT),
    PINK_WITCH_HAT(text("Pink Witch Hat", BlockColor.PINK.textColor), Mytems.PINK_WITCH_HAT),
    GRAY_WITCH_HAT(text("Gray Witch Hat", BlockColor.GRAY.textColor), Mytems.GRAY_WITCH_HAT),
    LIGHT_GRAY_WITCH_HAT(text("Light Gray Witch Hat", BlockColor.LIGHT_GRAY.textColor), Mytems.LIGHT_GRAY_WITCH_HAT),
    CYAN_WITCH_HAT(text("Cyan Witch Hat", BlockColor.CYAN.textColor), Mytems.CYAN_WITCH_HAT),
    PURPLE_WITCH_HAT(text("Purple Witch Hat", BlockColor.PURPLE.textColor), Mytems.PURPLE_WITCH_HAT),
    BLUE_WITCH_HAT(text("Blue Witch Hat", BlockColor.BLUE.textColor), Mytems.BLUE_WITCH_HAT),
    BROWN_WITCH_HAT(text("Brown Witch Hat", BlockColor.BROWN.textColor), Mytems.BROWN_WITCH_HAT),
    GREEN_WITCH_HAT(text("Green Witch Hat", BlockColor.GREEN.textColor), Mytems.GREEN_WITCH_HAT),
    RED_WITCH_HAT(text("Red Witch Hat", BlockColor.RED.textColor), Mytems.RED_WITCH_HAT),
    BLACK_WITCH_HAT(text("Black Witch Hat", BlockColor.BLACK.textColor), Mytems.BLACK_WITCH_HAT),
    // Cat Ears
    BLACK_CAT_EARS(text("Black Cat Ears", BlockColor.BLACK.textColor), Mytems.BLACK_CAT_EARS),
    CYAN_CAT_EARS(text("Cyan Cat Ears", BlockColor.CYAN.textColor), Mytems.CYAN_CAT_EARS),
    LIGHT_BLUE_CAT_EARS(text("Light Blue Cat Ears", BlockColor.LIGHT_BLUE.textColor), Mytems.LIGHT_BLUE_CAT_EARS),
    LIME_CAT_EARS(text("Lime Cat Ears", BlockColor.LIME.textColor), Mytems.LIME_CAT_EARS),
    ORANGE_CAT_EARS(text("Orange Cat Ears", BlockColor.ORANGE.textColor), Mytems.ORANGE_CAT_EARS),
    PINK_CAT_EARS(text("Pink Cat Ears", BlockColor.PINK.textColor), Mytems.PINK_CAT_EARS),
    RED_CAT_EARS(text("Red Cat Ears", BlockColor.RED.textColor), Mytems.RED_CAT_EARS),
    WHITE_CAT_EARS(text("White Cat Ears", BlockColor.WHITE.textColor), Mytems.WHITE_CAT_EARS),
    // Sunglasses
    BLACK_SUNGLASSES(text("Black Sunglasses", BlockColor.GRAY.textColor), Mytems.BLACK_SUNGLASSES),
    RED_SUNGLASSES(text("Red Sunglasses", BlockColor.RED.textColor), Mytems.RED_SUNGLASSES),
    GREEN_SUNGLASSES(text("Green Sunglasses", BlockColor.GREEN.textColor), Mytems.GREEN_SUNGLASSES),
    BLUE_SUNGLASSES(text("Blue Sunglasses", BlockColor.BLUE.textColor), Mytems.BLUE_SUNGLASSES),
    PURPLE_SUNGLASSES(text("Purple Sunglasses", BlockColor.PURPLE.textColor), Mytems.PURPLE_SUNGLASSES),
    CYAN_SUNGLASSES(text("Cyan Sunglasses", BlockColor.CYAN.textColor), Mytems.CYAN_SUNGLASSES),
    LIGHT_GRAY_SUNGLASSES(text("Light Gray Sunglasses", BlockColor.LIGHT_GRAY.textColor), Mytems.LIGHT_GRAY_SUNGLASSES),
    GRAY_SUNGLASSES(text("Gray Sunglasses", BlockColor.GRAY.textColor), Mytems.GRAY_SUNGLASSES),
    PINK_SUNGLASSES(text("Pink Sunglasses", BlockColor.PINK.textColor), Mytems.PINK_SUNGLASSES),
    LIME_SUNGLASSES(text("Lime Sunglasses", BlockColor.LIME.textColor), Mytems.LIME_SUNGLASSES),
    YELLOW_SUNGLASSES(text("Yellow Sunglasses", BlockColor.YELLOW.textColor), Mytems.YELLOW_SUNGLASSES),
    LIGHT_BLUE_SUNGLASSES(text("Light Blue Sunglasses", BlockColor.LIGHT_BLUE.textColor), Mytems.LIGHT_BLUE_SUNGLASSES),
    MAGENTA_SUNGLASSES(text("Magenta Sunglasses", BlockColor.MAGENTA.textColor), Mytems.MAGENTA_SUNGLASSES),
    ORANGE_SUNGLASSES(text("Orange Sunglasses", BlockColor.ORANGE.textColor), Mytems.ORANGE_SUNGLASSES),
    WHITE_SUNGLASSES(text("White Sunglasses", BlockColor.WHITE.textColor), Mytems.WHITE_SUNGLASSES),
    // Top Hat
    TOP_HAT(text("Top Hat", GRAY), Mytems.TOP_HAT),
    // Kobold
    KOBOLD_HEAD(text("Cobold Head", DARK_GREEN), Mytems.KOBOLD_HEAD),
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
                                         text("Helmet Item", DARK_PURPLE),
                                         text("Click to equip", TextColor.color(0xFFFF00))));
            });
        return itemStack;
    }

    @Override
    public List<Component> getMenuTooltip() {
        return List.of(getDisplayName(),
                       text("Hat", DARK_GRAY),
                       empty(),
                       textOfChildren(Mytems.MOUSE_LEFT, text(" Equip", GRAY)));
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event) {
        if (event.getClick() != ClickType.LEFT) return;
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1.0f, 1.0f);
        if (this == remove(player)) {
            player.sendMessage(text("Hat removed: ").color(WardrobeCommand.COLOR)
                               .append(displayName));
            return;
        }
        if (wear(player)) {
            player.sendMessage(text("Hat equipped: ").color(WardrobeCommand.COLOR)
                               .append(displayName));
        } else {
            player.sendMessage(text("Cannot equip ").color(TextColor.color(0xFF0000))
                               .append(displayName)
                               .append(text(": Inventory is full!").color(TextColor.color(0xFF0000))));
        }
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
