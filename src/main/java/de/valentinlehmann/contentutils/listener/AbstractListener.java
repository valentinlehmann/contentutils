package de.valentinlehmann.contentutils.listener;

import de.valentinlehmann.contentutils.ContentUtilsPlugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
@Getter
public abstract class AbstractListener implements Listener {
    private final ContentUtilsPlugin plugin;
}
