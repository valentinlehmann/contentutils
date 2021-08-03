package de.valentinlehmann.contentutils.core.player;

import de.valentinlehmann.contentutils.core.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class PlayerRepository implements Repository<ContentUtilsPlayer, UUID> {
    private final HashMap<UUID, ContentUtilsPlayer> storage = new HashMap<>();

    @Override
    public void add(ContentUtilsPlayer contentUtilsPlayer) {
        this.storage.put(contentUtilsPlayer.getUuid(), contentUtilsPlayer);
    }

    @Override
    public boolean contains(UUID key) {
        return this.storage.containsKey(key);
    }

    @Override
    public Collection<ContentUtilsPlayer> getAll() {
        return this.storage.values();
    }

    @Override
    public ContentUtilsPlayer get(UUID key) {
        return this.storage.get(key);
    }

    @Override
    public void update(UUID key, ContentUtilsPlayer contentUtilsPlayer) {
        this.storage.replace(key, contentUtilsPlayer);
    }

    @Override
    public void remove(UUID key) {
        this.storage.remove(key);
    }
}
