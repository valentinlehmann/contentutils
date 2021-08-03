package de.valentinlehmann.contentutils.command.impl;

import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.annotation.*;
import de.valentinlehmann.contentutils.ContentUtilsPlugin;
import de.valentinlehmann.contentutils.command.AbstractCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.stream.Collectors;

@CommandPermission("contentutils.itemflag")
@CommandAlias("itemflag")
public class ItemFlagCommand extends AbstractCommand {
    public ItemFlagCommand(ContentUtilsPlugin plugin) {
        super(plugin);

        getPlugin().getCommandManager().getCommandConditions().addCondition("any_item_flag_present", context -> {
            if (!context.getIssuer().getPlayer().getItemInHand().getItemMeta().getItemFlags().isEmpty()) {
                return;
            }

            throw new ConditionFailedException(getPlugin().getLocalizeUtils().getMessage("contentutils.command.itemflag.no-flag-present"));
        });

        getPlugin().getCommandManager().getCommandCompletions().registerStaticCompletion("itemflags", Arrays.stream(ItemFlag.values()).map(Enum::name).collect(Collectors.toList()));
        getPlugin().getCommandManager().getCommandCompletions().registerCompletion("present_itemflags", context -> {
            if (!context.getIssuer().isPlayer()) {
                return null;
            }

            if (context.getPlayer().getItemInHand() == null || context.getPlayer().getItemInHand().getType() == Material.AIR) {
                return null;
            }

            return context.getPlayer().getItemInHand().getItemMeta().getItemFlags().stream().map(Enum::name).collect(Collectors.toList());
        });
    }

    @Subcommand("add")
    @Conditions("item_in_hand")
    @CommandCompletion("@itemflags")
    public void handleAdd(Player player, ItemStack itemStack, ItemFlag itemFlag) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta.getItemFlags().contains(itemFlag)) {
            return;
        }

        itemMeta.addItemFlags(itemFlag);
        itemStack.setItemMeta(itemMeta);
        player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.itemflag.add", itemFlag.name()));
    }

    @Subcommand("remove")
    @Conditions("item_in_hand|any_item_flag_present")
    @CommandCompletion("@present_itemflags")
    public void handleRemove(Player player, ItemStack itemStack, ItemFlag itemFlag) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (!itemMeta.getItemFlags().contains(itemFlag)) {
            player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.itemflag.not-present"));
            return;
        }

        itemMeta.removeItemFlags(itemFlag);
        itemStack.setItemMeta(itemMeta);
        player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.itemflag.remove", itemFlag.name()));
    }

    @Subcommand("clear")
    @Conditions("item_in_hand|any_item_flag_present")
    public void handleClear(Player player, ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getItemFlags().forEach(itemMeta::removeItemFlags);
        itemStack.setItemMeta(itemMeta);
        player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.itemflag.clear"));
    }
}
