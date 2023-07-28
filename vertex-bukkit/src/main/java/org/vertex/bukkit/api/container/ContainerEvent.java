package org.vertex.bukkit.api.container;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.vertex.bukkit.gui.Container;

@AllArgsConstructor
public class ContainerEvent extends Event {

    @Getter private Container container;
    private static HandlerList handlerList = new HandlerList();

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
