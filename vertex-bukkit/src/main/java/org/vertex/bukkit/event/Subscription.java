package org.vertex.bukkit.event;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.vertex.bukkit.pipeline.PredicatePipeline;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class Subscription<T extends Event> {

    @Getter private PredicatePipeline<T> filters = new PredicatePipeline<>();
    @Getter private Consumer<T> handler;
    @Getter private Class<T> handled;
    @Getter private EventPriority priority;
    private boolean handleSubclasses;
    @Getter private boolean ignoreCanceled = false;

    public static <U extends Event>  Subscription<U> create(Class<U> event) {
        return new Subscription<>(event);
    }

    public Subscription(Class<T> event) {
        this.handled = event;
    }

    public boolean handleSubclasses() {
        return handleSubclasses;
    }

    public Subscription<T> withFilter(Predicate<T> filter) {
        this.filters.attach(filter);
        return this;
    }

    public Subscription<T> priority(EventPriority priority) {
        this.priority = priority;
        return this;
    }

    public Subscription<T> handler(Consumer<T> handler) {
        this.handler = handler;
        return this;
    }

    public Subscription<T> ignoreCancelled() {
        this.ignoreCanceled = !ignoreCanceled;
        return this;
    }
}
