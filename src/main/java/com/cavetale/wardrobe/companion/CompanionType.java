package com.cavetale.wardrobe.companion;

import com.cavetale.mytems.Mytems;
import com.cavetale.wardrobe.Category;
import com.cavetale.wardrobe.WardrobeItem;
import com.cavetale.wardrobe.session.Session;
import java.util.List;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import static com.cavetale.wardrobe.WardrobePlugin.sessionOf;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextColor.color;

@RequiredArgsConstructor
public enum CompanionType implements WardrobeItem {
    EASTER_EGGS(text("Easter Eggs", color(0x80CEE1)), EasterEggsCompanion::new, Mytems.EASTER_EGG::createIcon),
    BLUE_BUTTERFLY(text("Blue Butterfly", GOLD), ButterflyCompanion.Blue::new, Mytems.BLUE_BUTTERFLY::createIcon),
    CYAN_BUTTERFLY(text("Cyan Butterfly", GOLD), ButterflyCompanion.Cyan::new, Mytems.CYAN_BUTTERFLY::createIcon),
    GREEN_BUTTERFLY(text("Green Butterfly", GOLD), ButterflyCompanion.Green::new, Mytems.GREEN_BUTTERFLY::createIcon),
    ORANGE_BUTTERFLY(text("Orange Butterfly", GOLD), ButterflyCompanion.Orange::new, Mytems.ORANGE_BUTTERFLY::createIcon),
    PINK_BUTTERFLY(text("Pink Butterfly", GOLD), ButterflyCompanion.Pink::new, Mytems.PINK_BUTTERFLY::createIcon),
    PURPLE_BUTTERFLY(text("Purple Butterfly", GOLD), ButterflyCompanion.Purple::new, Mytems.PURPLE_BUTTERFLY::createIcon),
    YELLOW_BUTTERFLY(text("Yellow Butterfly", GOLD), ButterflyCompanion.Yellow::new, Mytems.YELLOW_BUTTERFLY::createIcon),
    ;

    @Getter public final Component displayName;
    private final Supplier<Companion> companionSupplier;
    private final Supplier<ItemStack> iconSupplier;

    @Override
    public ItemStack toMenuItem() {
        return iconSupplier.get();
    }

    @Override
    public List<Component> getMenuTooltip() {
        return List.of(getDisplayName(),
                       text("Companion", DARK_GRAY),
                       empty(),
                       textOfChildren(Mytems.MOUSE_LEFT, text(" Activate", GRAY)));
    }

    @Override
    public void onClick(Player player, InventoryClickEvent event) {
        if (event.getClick() != ClickType.LEFT) return;
        Session session = sessionOf(player);
        if (session.getCompanion() != null && session.getCompanion().getType() == this) {
            session.getCompanion().stop();
            session.setCompanion(null);
            session.saveCompanion();
        } else {
            equip(session, player);
            session.saveCompanion();
        }
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1.0f, 1.0f);
    }

    /**
     * Equip this type.  Called by this and Session.
     */
    public Companion equip(Session session, Player player) {
        if (session.getCompanion() != null) {
            session.getCompanion().stop();
            session.setCompanion(null);
        }
        Companion companion = companionSupplier.get();
        session.setCompanion(companion);
        companion.start(player);
        return companion;
    }

    @Override
    public Category getCategory() {
        return Category.COMPANION;
    }

    @Override
    public boolean isWearing(Player player) {
        Session session = sessionOf(player);
        return session.getCompanion() != null && session.getCompanion().getType() == this;
    }

    @Override
    public int getIndex() {
        return ordinal();
    }

    public static CompanionType of(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException iae) {
            return null;
        }
    }
}
