package de.valentinlehmann.contentutils.core.inventory;

import de.valentinlehmann.contentutils.ContentUtilsPlugin;
import de.valentinlehmann.contentutils.core.Repository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

@RequiredArgsConstructor
@Getter
public class InventoryRepository implements Repository<SerializableInventory, String> {
    private final ContentUtilsPlugin plugin;
    private final HashMap<String, SerializableInventory> storage = new HashMap<>();
    private final HashMap<String, Inventory> inventoryCache = new HashMap<>();

    @Override
    public void startup() {
        Repository.super.startup();

        File inventoryFolder = new File(this.plugin.getDataFolder(), "inventories");

        if (!inventoryFolder.exists()) {
            inventoryFolder.mkdirs();
            return;
        }

        for (File file : Objects.requireNonNull(inventoryFolder.listFiles())) {
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            SerializableInventory serializableInventory = SerializableInventory.deserialize(configuration.getValues(true));

            this.storage.put(serializableInventory.getName(), serializableInventory);
            this.inventoryCache.put(serializableInventory.getName(), serializableInventory.toBukkitInventory());
        }
    }

    @Override
    public void shutdown() {
        Repository.super.shutdown();

        File inventoryFolder = new File(this.plugin.getDataFolder(), "inventories");

        this.storage.values().forEach(serializableInventory -> {
            YamlConfiguration configuration = new YamlConfiguration();
            serializableInventory.serialize().forEach(configuration::set);

            try {
                configuration.save(new File(inventoryFolder, serializableInventory.getName() + ".yml"));
            } catch (IOException e) {
                this.plugin.getLogger().severe(String.format("Konnte das Inventar %s nicht speichern!", serializableInventory.getName()));
                e.printStackTrace();
            }
        });
    }

    @Override
    public void add(SerializableInventory serializableInventory) {
        this.storage.put(serializableInventory.getName(), serializableInventory);
        this.inventoryCache.put(serializableInventory.getName(), serializableInventory.toBukkitInventory());
    }

    @Override
    public boolean contains(String key) {
        return this.storage.containsKey(key);
    }

    @Override
    public Collection<SerializableInventory> getAll() {
        return this.storage.values();
    }

    @Override
    public SerializableInventory get(String key) {
        return this.storage.get(key);
    }

    public void openInventory(Player player, String key) {
        player.openInventory(this.inventoryCache.get(key));
    }

    @Override
    public void update(String key, SerializableInventory serializableInventory) {
        this.storage.replace(key, serializableInventory);
        this.inventoryCache.get(key).getViewers().forEach(HumanEntity::closeInventory);
        this.inventoryCache.replace(key, serializableInventory.toBukkitInventory());
    }

    @Override
    public void remove(String key) {
        this.storage.remove(key);
    }
}
