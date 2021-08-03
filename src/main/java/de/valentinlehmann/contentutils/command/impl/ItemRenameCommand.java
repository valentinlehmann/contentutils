package de.valentinlehmann.contentutils.command.impl;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Conditions;
import co.aikar.commands.annotation.Default;
import de.valentinlehmann.contentutils.ContentUtilsPlugin;
import de.valentinlehmann.contentutils.command.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@CommandPermission("contentutils.itemrename")
@CommandAlias("itemrename")
public class ItemRenameCommand extends AbstractCommand {
    public ItemRenameCommand(ContentUtilsPlugin plugin) {
        super(plugin);
    }

    @Default
    @Conditions("item_in_hand")
    public void handleDefault(Player player, ItemStack itemStack, String name) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        itemStack.setItemMeta(itemMeta);
        player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.itemrename.done", itemMeta.getDisplayName()));
    }
}
