package de.valentinlehmann.contentutils;

import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.PaperCommandManager;
import de.valentinlehmann.contentutils.command.AbstractCommand;
import de.valentinlehmann.contentutils.core.inventory.InventoryRepository;
import de.valentinlehmann.contentutils.core.player.PlayerRepository;
import de.valentinlehmann.contentutils.listener.AbstractListener;
import de.valentinlehmann.contentutils.utils.ClassPathUtils;
import de.valentinlehmann.contentutils.utils.LocalizeUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class ContentUtilsPlugin extends JavaPlugin {
    private final InventoryRepository inventoryRepository = new InventoryRepository(this);
    private final PlayerRepository playerRepository = new PlayerRepository();
    private final ClassPathUtils classPathUtils = new ClassPathUtils(this);
    private final LocalizeUtils localizeUtils = new LocalizeUtils(this);
    private PaperCommandManager commandManager;

    @Override
    public void onEnable() {
        super.onEnable();

        this.commandManager = new PaperCommandManager(this);
        this.commandManager.enableUnstableAPI("help");
        this.commandManager.getCommandConditions().addCondition("item_in_hand", context -> {
            if (context.getIssuer().isPlayer()
                    && context.getIssuer().getPlayer().getItemInHand() != null
                    && context.getIssuer().getPlayer().getItemInHand().getType() != Material.AIR) {
                return;
            }

            throw new ConditionFailedException(this.localizeUtils.getMessage("contentutils.command.no-item-in-hand"));
        });
        this.commandManager.getCommandContexts().registerOptionalContext(ItemStack.class,
                context -> context.getPlayer().getItemInHand());

        this.classPathUtils.createInstanceAndApplyAction("de.valentinlehmann.contentutils.command.impl",
                AbstractCommand.class, command -> this.commandManager.registerCommand(command), this);
        this.classPathUtils.createInstanceAndApplyAction("de.valentinlehmann.contentutils.listener.impl",
                AbstractListener.class, listener -> Bukkit.getPluginManager().registerEvents(listener, this), this);

        this.localizeUtils.loadMessages();
        this.inventoryRepository.startup();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        this.inventoryRepository.shutdown();
    }
}
