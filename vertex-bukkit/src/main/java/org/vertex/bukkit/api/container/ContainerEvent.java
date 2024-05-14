package org.vertex.bukkit.api.container;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.vertex.bukkit.gui.Container;

public class ContainerEvent extends Event {

    private Container container;
    private static HandlerList handlerList = new HandlerList();

    public ContainerEvent(Container container) {
        this.container = container;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public Container getContainer() {
        return container;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
