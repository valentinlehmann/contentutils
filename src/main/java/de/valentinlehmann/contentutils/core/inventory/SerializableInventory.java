package de.valentinlehmann.contentutils.core.inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@SerializableAs("ContentUtilsInventory")
@Setter
public class SerializableInventory implements ConfigurationSerializable {
    private final String name;
    private String title;
    private int rows;
    private ItemStack[] itemStacks;

    public Inventory toBukkitInventory() {
        Inventory inventory = Bukkit.createInventory(null, this.rows * 9, this.title);
        inventory.setContents(Arrays.copyOfRange(this.itemStacks, 0, Math.min(inventory.getSize(), this.itemStacks.length)));

        return inventory;
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("name", this.name);
        map.put("title", this.title);
        map.put("rows", this.rows);
        map.put("items", this.itemStacks);

        return map;
    }

    public static SerializableInventory fromBukkitInventory(String name, Inventory inventory) {
        return new SerializableInventory(name, inventory.getTitle(),inventory.getSize() / 9, inventory.getContents());
    }

    public static SerializableInventory deserialize(Map<String, Object> map) {
        String name = (String) map.get("name");
        String title = (String) map.get("title");
        int rows = (int) map.get("rows");
        List<ItemStack> items = (List<ItemStack>) map.get("items");

        return new SerializableInventory(name, title, rows, items.toArray(new ItemStack[] {}));
    }
}
