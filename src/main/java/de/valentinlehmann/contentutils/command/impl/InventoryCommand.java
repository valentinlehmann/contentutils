package de.valentinlehmann.contentutils.command.impl;

import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.annotation.*;
import de.valentinlehmann.contentutils.ContentUtilsPlugin;
import de.valentinlehmann.contentutils.command.AbstractCommand;
import de.valentinlehmann.contentutils.core.inventory.SerializableInventory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

@CommandPermission("contentutils.inventory")
@CommandAlias("inventory")
public class InventoryCommand extends AbstractCommand {
    public InventoryCommand(ContentUtilsPlugin plugin) {
        super(plugin);

        getPlugin().getCommandManager().getCommandConditions().addCondition("inventory_selected", context -> {
            if (getPlugin().getPlayerRepository().get(context.getIssuer().getUniqueId()).getSelectedInventory() != null) {
                return;
            }

            throw new ConditionFailedException(getPlugin().getLocalizeUtils().getMessage("contentutils.command.inventory.no-edit-inventory-raw"));
        });

        getPlugin().getCommandManager().getCommandContexts().registerIssuerOnlyContext(SerializableInventory.class,
                bukkitCommandExecutionContext -> getPlugin().getPlayerRepository()
                        .get(bukkitCommandExecutionContext.getPlayer().getUniqueId()).getSelectedInventory());

        getPlugin().getCommandManager().getCommandCompletions().registerCompletion("inventories", context -> getPlugin().getInventoryRepository().getAll().stream().map(SerializableInventory::getName).collect(Collectors.toList()));
    }

    @Subcommand("create")
    public void handleCreate(Player player, String name) {
        if (getPlugin().getInventoryRepository().contains(name)) {
            player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.inventory.already-existing"));
            return;
        }

        getPlugin().getInventoryRepository().add(SerializableInventory.fromBukkitInventory(name, Bukkit.createInventory(null, 3 * 9)));
        player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.inventory.created", name));
    }

    @Subcommand("edit")
    @CommandCompletion("@inventories")
    public void handleEdit(Player player, String name) {
        if (name.equalsIgnoreCase("off")) {
            getPlugin().getPlayerRepository().get(player.getUniqueId()).setSelectedInventory(null);
            player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.inventory.edit-off"));
        } else {
            getPlugin().getPlayerRepository().get(player.getUniqueId()).setSelectedInventory(getPlugin().getInventoryRepository().get(name));
            player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.inventory.edit", name));
        }
    }

    @Subcommand("open")
    @CommandCompletion("@inventories")
    public void handleOpen(Player player, @Default("current") String name) {
        if (name.equalsIgnoreCase("current")) {
            SerializableInventory selectedInventory = getPlugin().getPlayerRepository().get(player.getUniqueId()).getSelectedInventory();

            if (selectedInventory == null) {
                player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.inventory.no-edit-inventory"));
                return;
            }

            getPlugin().getInventoryRepository().openInventory(player, selectedInventory.getName());
            return;
        }

        if (!getPlugin().getInventoryRepository().contains(name)) {
            player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.inventory.no-such-inventory"));
            return;
        }

        getPlugin().getInventoryRepository().openInventory(player, name);
    }

    @Subcommand("setsize")
    @Conditions("inventory_selected")
    public void handleSetSize(Player player, SerializableInventory serializableInventory, int rows) {
        serializableInventory.setRows(rows);
        getPlugin().getInventoryRepository().update(serializableInventory.getName(), serializableInventory);
        player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.inventory.set-size", rows));
    }

    @Subcommand("settitle")
    @Conditions("inventory_selected")
    public void handleSetTitle(Player player, SerializableInventory serializableInventory, String title) {
        serializableInventory.setTitle(ChatColor.translateAlternateColorCodes('&', title));
        getPlugin().getInventoryRepository().update(serializableInventory.getName(), serializableInventory);
        player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.inventory.set-title", serializableInventory.getTitle()));
    }

    @Subcommand("saveitems")
    @Conditions("inventory_selected")
    public void handleSaveItems(Player player, SerializableInventory serializableInventory) {
        serializableInventory.setItemStacks(getPlugin().getInventoryRepository().getInventoryCache().get(serializableInventory.getName()).getContents());
        getPlugin().getInventoryRepository().update(serializableInventory.getName(), serializableInventory);
        player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.inventory.items-saved"));
    }

    @Subcommand("delete")
    @CommandCompletion("@inventories")
    public void handleDelete(Player player, String name) {
        if (!getPlugin().getInventoryRepository().contains(name)) {
            player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.inventory.no-such-inventory"));
            return;
        }

        getPlugin().getPlayerRepository().getAll().stream()
                .filter(contentUtilsPlayer -> contentUtilsPlayer.getSelectedInventory().getName().equals(name))
                .forEach(contentUtilsPlayer -> contentUtilsPlayer.setSelectedInventory(null));

        getPlugin().getInventoryRepository().remove(name);
        player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.inventory.deleted", name));
    }
}
