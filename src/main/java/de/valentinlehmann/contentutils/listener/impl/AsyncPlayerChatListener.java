package de.valentinlehmann.contentutils.listener.impl;

import de.valentinlehmann.contentutils.ContentUtilsPlugin;
import de.valentinlehmann.contentutils.listener.AbstractListener;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener extends AbstractListener {
    public AsyncPlayerChatListener(ContentUtilsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        if (!getPlugin().getPlayerRepository().get(event.getPlayer().getUniqueId()).isChatToggled()) {
            return;
        }

        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
        event.setCancelled(true);
    }
}
