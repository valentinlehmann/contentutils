package de.valentinlehmann.contentutils.core.player;

import de.valentinlehmann.contentutils.core.inventory.SerializableInventory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class ContentUtilsPlayer {
    private final UUID uuid;
    private boolean chatToggled;
    private SerializableInventory selectedInventory;
}
