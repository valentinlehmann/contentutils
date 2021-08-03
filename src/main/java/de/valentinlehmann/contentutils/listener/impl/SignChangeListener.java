package de.valentinlehmann.contentutils.listener.impl;

import de.valentinlehmann.contentutils.ContentUtilsPlugin;
import de.valentinlehmann.contentutils.listener.AbstractListener;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeListener extends AbstractListener {
    public SignChangeListener(ContentUtilsPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        for (int i = 0; i < event.getLines().length; i++) {
            event.setLine(i, ChatColor.translateAlternateColorCodes('&', event.getLine(i)));
        }
    }
}
