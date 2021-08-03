package de.valentinlehmann.contentutils.command.impl;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import de.valentinlehmann.contentutils.ContentUtilsPlugin;
import de.valentinlehmann.contentutils.command.AbstractCommand;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CommandPermission("contentutils.sound")
@CommandAlias("sound")
public class SoundCommand extends AbstractCommand {
    private final List<String> soundNames;

    public SoundCommand(ContentUtilsPlugin contentUtilsPlugin) {
        super(contentUtilsPlugin);

        this.soundNames = Arrays.stream(Sound.values()).map(Sound::name).collect(Collectors.toList());

        getPlugin().getCommandManager().getCommandCompletions().registerStaticCompletion("sounds", soundNames);
    }

    @Default
    @CommandCompletion("@sounds")
    public void handleDefault(Player player, String sound, @Default("1.0") String pitch) {
        sound = sound.toUpperCase();

        if (!this.soundNames.contains(sound)) {
            player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.sound.sound-not-found"));
            return;
        }

        float pitchFloat = 1.0F;

        try {
            pitchFloat = Float.parseFloat(pitch);
        } catch (NumberFormatException e) {
            player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.sound.pitch-not-valid"));
        }

        player.playSound(player.getLocation(), Sound.valueOf(sound), 1.0F, pitchFloat);
    }
}
