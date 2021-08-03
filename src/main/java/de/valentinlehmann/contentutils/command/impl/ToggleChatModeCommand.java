package de.valentinlehmann.contentutils.command.impl;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import de.valentinlehmann.contentutils.ContentUtilsPlugin;
import de.valentinlehmann.contentutils.command.AbstractCommand;
import de.valentinlehmann.contentutils.core.player.ContentUtilsPlayer;
import org.bukkit.entity.Player;

@CommandPermission("contentutils.togglechat")
@CommandAlias("togglechat|toggle")
public class ToggleChatModeCommand extends AbstractCommand {
    public ToggleChatModeCommand(ContentUtilsPlugin contentUtilsPlugin) {
        super(contentUtilsPlugin);
    }

    @Default
    public void handleToggleChatMode(Player player) {
        ContentUtilsPlayer contentUtilsPlayer = getPlugin().getPlayerRepository().get(player.getUniqueId());

        contentUtilsPlayer.setChatToggled(!contentUtilsPlayer.isChatToggled());
        getPlugin().getPlayerRepository().update(player.getUniqueId(), contentUtilsPlayer);

        if (contentUtilsPlayer.isChatToggled()) {
            player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.toggle-chat.enabled"));
        } else {
            player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.toggle-chat.disabled"));
        }
    }
}
