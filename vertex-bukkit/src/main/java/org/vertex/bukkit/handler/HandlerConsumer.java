package org.vertex.bukkit.handler;

import org.bukkit.event.Event;

public interface HandlerConsumer<T extends Event> {
    void handle(T event);
}
