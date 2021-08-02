package de.valentinlehmann.contentutils;

import co.aikar.commands.PaperCommandManager;
import de.valentinlehmann.contentutils.command.impl.SoundCommand;
import de.valentinlehmann.contentutils.utils.LocalizeUtils;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class ContentUtilsPlugin extends JavaPlugin {
    private final LocalizeUtils localizeUtils = new LocalizeUtils(this);
    private PaperCommandManager commandManager;

    @Override
    public void onEnable() {
        super.onEnable();

        this.commandManager = new PaperCommandManager(this);
        this.commandManager.registerCommand(new SoundCommand(this));

        this.localizeUtils.loadMessages();
    }
}
