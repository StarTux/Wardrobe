package com.cavetale.wardrobe.session;

import com.cavetale.wardrobe.WardrobePlugin;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created and managed by WardrobePlugin.
 */
@RequiredArgsConstructor
public final class Sessions implements Listener {
    protected final WardrobePlugin plugin;
    protected final Map<UUID, Session> sessionsMap = new HashMap<>();

    public void enable() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        for (Player player : Bukkit.getOnlinePlayers()) {
            Session session = new Session(player);
            sessionsMap.put(session.uuid, session);
            session.enable();
        }
    }

    public void disable() {
        for (Session session : sessionsMap.values()) {
            session.disable();
        }
        sessionsMap.clear();
    }

    public Session of(Player player) {
        return sessionsMap.get(player.getUniqueId());
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Session session = new Session(event.getPlayer());
        sessionsMap.put(session.uuid, session);
        session.enable();
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Session session = sessionsMap.remove(event.getPlayer().getUniqueId());
        if (session != null) session.disable();
    }
}
