package de.valentinlehmann.contentutils.listener.impl;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import de.valentinlehmann.contentutils.ContentUtilsPlugin;
import de.valentinlehmann.contentutils.listener.AbstractListener;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class PlayerInteractListener extends AbstractListener {
    private final HashMap<Player, Block> currentSign;

    public PlayerInteractListener(ContentUtilsPlugin plugin) {
        super(plugin);

        this.currentSign = new HashMap<>();

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(getPlugin(), PacketType.Play.Client.UPDATE_SIGN) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                handleUpdateSign(event, event.getPlayer(), event.getPacket());
            }
        });
    }

    private void handleUpdateSign(PacketEvent event, Player player, PacketContainer packet) {
        if (!this.currentSign.containsKey(player)) {
            return;
        }

        event.setCancelled(true);

        Block block = this.currentSign.get(player);
        this.currentSign.remove(player);

        if (block.getType() != Material.SIGN && block.getType() != Material.SIGN_POST) {
            return;
        }

        Sign sign = (Sign) block.getState();
        WrappedChatComponent[] lines = packet.getChatComponentArrays().read(0);

        for (int i = 0; i < lines.length; i++) {
            sign.setLine(i, ChatColor.translateAlternateColorCodes('&',
                    BaseComponent.toPlainText(ComponentSerializer.parse(lines[i].getJson()))));
        }

        sign.update(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block block = event.getClickedBlock();

        if (block.getType() != Material.WALL_SIGN && block.getType() != Material.SIGN_POST) {
            return;
        }

        this.currentSign.put(event.getPlayer(), event.getClickedBlock());

        BlockPosition blockPosition = new BlockPosition(block.getX(), block.getY(), block.getZ());
        Sign sign = (Sign) block.getState();
        WrappedChatComponent[] lines = new WrappedChatComponent[sign.getLines().length];

        // Changing § in lines to & to retain current color codes (only for editing player)
        for (int i = 0; i < sign.getLines().length; i++) {
            lines[i] = WrappedChatComponent.fromText(sign.getLine(i).replace("§", "&"));
        }

        PacketContainer updateSignPacket = new PacketContainer(PacketType.Play.Server.UPDATE_SIGN);
        updateSignPacket.getBlockPositionModifier().write(0, blockPosition);
        updateSignPacket.getChatComponentArrays().write(0, lines);

        sendPacket(event.getPlayer(), updateSignPacket);

        PacketContainer signEditPacket = new PacketContainer(PacketType.Play.Server.OPEN_SIGN_EDITOR);
        signEditPacket.getBlockPositionModifier().write(0, blockPosition);

        sendPacket(event.getPlayer(), signEditPacket);
    }

    private void sendPacket(Player player, PacketContainer packetContainer) {
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packetContainer);
        } catch (InvocationTargetException e) {
            getPlugin().getLogger().severe(String.format("Konnte das Packet %s nicht an den Spieler %s senden!",
                    packetContainer.getType().name(), player.getName()));
            e.printStackTrace();
        }
    }
}
