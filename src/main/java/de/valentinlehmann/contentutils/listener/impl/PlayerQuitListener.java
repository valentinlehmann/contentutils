package de.valentinlehmann.contentutils.listener.impl;

import de.valentinlehmann.contentutils.ContentUtilsPlugin;
import de.valentinlehmann.contentutils.listener.AbstractListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener extends AbstractListener {
    public PlayerQuitListener(ContentUtilsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        getPlugin().getPlayerRepository().remove(event.getPlayer().getUniqueId());
    }
}
