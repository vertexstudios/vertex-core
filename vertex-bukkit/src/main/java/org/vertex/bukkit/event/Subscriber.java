package org.vertex.bukkit.event;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface Subscriber extends Listener, EventExecutor {
    void dispose();
    void subscribe();
    boolean disposed();

    public static interface Builder<EVENT extends Event> {
        Builder<EVENT> create();
        Builder<EVENT> withFilter(Predicate<EVENT> filter);
        Builder<EVENT> handle(Consumer<EVENT> s);
        Builder<EVENT> forEvent(Class<EVENT> clazz);
        Builder<EVENT> priority(EventPriority priority);
        Builder<EVENT> handleSubclasses();
        Builder<EVENT> ignoreCancelled();
        Subscriber build();
    }

}
