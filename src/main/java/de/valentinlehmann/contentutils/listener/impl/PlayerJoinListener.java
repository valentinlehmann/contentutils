package de.valentinlehmann.contentutils.listener.impl;

import de.valentinlehmann.contentutils.ContentUtilsPlugin;
import de.valentinlehmann.contentutils.core.player.ContentUtilsPlayer;
import de.valentinlehmann.contentutils.listener.AbstractListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener extends AbstractListener {
    public PlayerJoinListener(ContentUtilsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        getPlugin().getPlayerRepository().add(new ContentUtilsPlayer(event.getPlayer().getUniqueId(), false, null));
    }
}
