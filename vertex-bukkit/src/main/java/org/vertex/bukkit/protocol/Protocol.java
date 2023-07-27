package org.vertex.bukkit.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import lombok.Builder;
import org.bukkit.entity.Player;
import org.vertex.bukkit.BukkitPluginContainer;
import org.vertex.bukkit.VertexBukkitPlugin;

public class Protocol {

    @Builder
    public static class UpdateScreen {
        private String title;
        private int containerId;
        private int windowType;

        public void send(Player player) {
            PacketContainer update = new PacketContainer(PacketType.Play.Server.OPEN_WINDOW);
            update.getIntegers().write(0, containerId).write(1, windowType);
            update.getChatComponents().write(0, WrappedChatComponent.fromText(title));
            ((VertexBukkitPlugin)BukkitPluginContainer.getCurrentPlugin()).getProtocolManager().sendServerPacket(player, update);
        }
    }

}
