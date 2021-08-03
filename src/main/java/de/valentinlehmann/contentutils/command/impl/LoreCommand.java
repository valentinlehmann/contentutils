package de.valentinlehmann.contentutils.command.impl;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Conditions;
import co.aikar.commands.annotation.Subcommand;
import de.valentinlehmann.contentutils.ContentUtilsPlugin;
import de.valentinlehmann.contentutils.command.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@CommandPermission("contentutils.lore")
@CommandAlias("lore")
public class LoreCommand extends AbstractCommand {
    public LoreCommand(ContentUtilsPlugin plugin) {
        super(plugin);
    }

    @Subcommand("add")
    @Conditions("item_in_hand")
    public void handleAdd(Player player, ItemStack itemStack, String lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> loreLines;

        if (itemMeta.hasLore()) {
            loreLines = itemMeta.getLore();
        } else {
            loreLines = new ArrayList<>();
        }

        loreLines.add(prepareLoreLine(lore));
        itemMeta.setLore(loreLines);
        itemStack.setItemMeta(itemMeta);

        player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.lore.add", loreLines.get(loreLines.size() - 1)));
    }

    @Subcommand("remove")
    @Conditions("item_in_hand")
    public void handleRemove(Player player, ItemStack itemStack, int line) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> loreLines;

        if (!itemMeta.hasLore()) {
            player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.lore.no-lore"));
            return;
        } else {
            loreLines = itemMeta.getLore();
        }

        if (line < 1 || line > loreLines.size()) {
            player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.lore.line-not-existing"));
            return;
        }

        loreLines.remove(line - 1);
        itemMeta.setLore(loreLines);
        itemStack.setItemMeta(itemMeta);

        player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.lore.remove", line));
    }

    @Subcommand("set")
    @Conditions("item_in_hand")
    public void handleSet(Player player, ItemStack itemStack, int line, String lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> loreLines;

        if (!itemMeta.hasLore()) {
            loreLines = new ArrayList<>();
        } else {
            loreLines = itemMeta.getLore();
        }

        if (line < 1 || line > loreLines.size()) {
            player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.lore.line-not-existing"));
            return;
        }

        loreLines.set(line - 1, prepareLoreLine(lore));
        itemMeta.setLore(loreLines);
        itemStack.setItemMeta(itemMeta);

        player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.lore.set", line, loreLines.get(line - 1)));
    }

    @Subcommand("insert")
    @Conditions("item_in_hand")
    public void handleInsert(Player player, ItemStack itemStack, int line, String lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> loreLines;

        if (!itemMeta.hasLore()) {
            loreLines = new ArrayList<>();
        } else {
            loreLines = itemMeta.getLore();
        }

        if (line < 1 || line > loreLines.size()) {
            player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.lore.line-not-existing"));
            return;
        }

        loreLines.add(line - 1, prepareLoreLine(lore));
        itemMeta.setLore(loreLines);
        itemStack.setItemMeta(itemMeta);

        player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.lore.insert", line, loreLines.get(line - 1)));
    }

    @Subcommand("clear")
    @Conditions("item_in_hand")
    public void handleClear(Player player, ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (!itemMeta.hasLore()) {
            player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.lore.no-lore"));
            return;
        }

        itemMeta.setLore(null);
        itemStack.setItemMeta(itemMeta);

        player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.lore.clear"));
    }

    private String prepareLoreLine(String loreLine) {
        return ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', loreLine);
    }
}
