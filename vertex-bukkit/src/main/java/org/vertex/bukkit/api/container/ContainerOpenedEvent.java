package org.vertex.bukkit.api.container;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.vertex.bukkit.gui.Container;

public class ContainerOpenedEvent extends ContainerEvent  {

    private static HandlerList handlerList = new HandlerList();

    public ContainerOpenedEvent(Container container) {
        super(container);
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
