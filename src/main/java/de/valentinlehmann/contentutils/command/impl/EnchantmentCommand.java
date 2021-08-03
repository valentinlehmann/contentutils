package de.valentinlehmann.contentutils.command.impl;

import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.annotation.*;
import de.valentinlehmann.contentutils.ContentUtilsPlugin;
import de.valentinlehmann.contentutils.command.AbstractCommand;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.HashMap;

@CommandPermission("contentutils.enchantment")
@CommandAlias("enchantment")
public class EnchantmentCommand extends AbstractCommand {
    public EnchantmentCommand(ContentUtilsPlugin plugin) {
        super(plugin);

        try {
            Field mapField = Enchantment.class.getDeclaredField("byName");
            mapField.setAccessible(true);
            HashMap<String, Enchantment> enchantments = (HashMap<String, Enchantment>) mapField.get(null);

            getPlugin().getCommandManager().getCommandCompletions().registerStaticCompletion("enchantments", enchantments.keySet());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            getPlugin().getLogger().severe("Es ist ein Fehler beim Laden der Enchantments aufgetreten!");
            e.printStackTrace();
        }

        getPlugin().getCommandManager().getCommandContexts().registerIssuerAwareContext(Enchantment.class,
                bukkitCommandExecutionContext -> Enchantment.getByName(bukkitCommandExecutionContext.popFirstArg()));

        getPlugin().getCommandManager().getCommandConditions().addCondition("any_enchantment_present", context -> {
            if (context.getIssuer().getPlayer().getItemInHand().getEnchantments().size() > 0) {
                return;
            }

            throw new ConditionFailedException(getPlugin().getLocalizeUtils().getMessage("contentutils.command.enchantment.no-enchantments"));
        });
    }

    @Subcommand("add")
    @Conditions("item_in_hand")
    @CommandCompletion("@enchantments")
    public void handleAdd(Player player, ItemStack itemStack, Enchantment enchantment, int level) {
        if (isFalseEnchantment(player, enchantment)) {
            return;
        }

        itemStack.addUnsafeEnchantment(enchantment, level);
        player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.enchantment.add", enchantment.getName()));
    }

    @Subcommand("remove")
    @Conditions("item_in_hand|any_enchantment_present")
    @CommandCompletion("@enchantments")
    public void handleRemove(Player player, ItemStack itemStack, Enchantment enchantment) {
        if (isFalseEnchantment(player, enchantment)) {
            return;
        }

        if (!itemStack.containsEnchantment(enchantment)) {
            player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.enchantment.not-present", enchantment.getName()));
            return;
        }

        itemStack.removeEnchantment(enchantment);
        player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.enchantment.removed", enchantment.getName()));
    }

    @Subcommand("clear")
    @Conditions("item_in_hand|any_enchantment_present")
    @CommandCompletion("@enchantments")
    public void handleClear(Player player, ItemStack itemStack) {
        itemStack.getEnchantments().keySet().forEach(itemStack::removeEnchantment);
        player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.enchantment.cleared"));
    }

    private boolean isFalseEnchantment(Player player, Enchantment enchantment) {
        if (enchantment != null) {
            return false;
        }

        player.sendMessage(getPlugin().getLocalizeUtils().getMessage("contentutils.command.enchantment.not-existing"));
        return true;
    }
}
