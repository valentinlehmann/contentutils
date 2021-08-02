package de.valentinlehmann.contentutils.command;

import co.aikar.commands.BaseCommand;
import de.valentinlehmann.contentutils.ContentUtilsPlugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class AbstractCommand extends BaseCommand {
    private final ContentUtilsPlugin plugin;
}
