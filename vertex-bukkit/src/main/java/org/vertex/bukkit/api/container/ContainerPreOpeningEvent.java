package org.vertex.bukkit.api.container;

import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.vertex.bukkit.gui.Container;

public class ContainerPreOpeningEvent extends ContainerEvent implements Cancellable {

    private static HandlerList handlerList = new HandlerList();
    private boolean canceled = false;

    public ContainerPreOpeningEvent(Container container) {
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

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        canceled = cancel;
    }
}
