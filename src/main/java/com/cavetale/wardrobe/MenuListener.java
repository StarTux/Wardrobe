package com.cavetale.wardrobe;

import com.cavetale.core.menu.MenuItemEntry;
import com.cavetale.core.menu.MenuItemEvent;
import com.cavetale.mytems.Mytems;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

@RequiredArgsConstructor
public final class MenuListener implements Listener {
    public static final String MENU_KEY = "wardrobe:wardrobe";
    public static final String MENU_PERMISSION = "wardrobe.wardrobe";
    private final WardrobePlugin plugin;

    protected void enable() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    private void onMenuItem(MenuItemEvent event) {
        if (!event.getPlayer().hasPermission(MENU_PERMISSION)) {
            return;
        }
        event.addItem(builder -> builder
                      .priority(MenuItemEntry.Priority.REGULAR)
                      .key(MENU_KEY)
                      .command("wardrobe")
                      .icon(Mytems.KOBOLD_HEAD.createIcon(List.of(text("Wardrobe", LIGHT_PURPLE)))));
    }
}
