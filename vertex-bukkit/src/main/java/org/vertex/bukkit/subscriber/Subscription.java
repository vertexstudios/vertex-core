package org.vertex.bukkit.subscriber;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.vertex.bukkit.pipeline.PredicatePipeline;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class Subscription<T extends Event> {

    private PredicatePipeline<T> filters = new PredicatePipeline<>();
    private Consumer<T> handler;
    private Class<T> handled;
    private EventPriority priority = EventPriority.NORMAL;
    private boolean handleSubclasses;
    private boolean ignoreCanceled = false;

    public PredicatePipeline<T> getFilters() {
        return filters;
    }

    public Class<T> getHandled() {
        return handled;
    }

    public Consumer<T> getHandler() {
        return handler;
    }

    public EventPriority getPriority() {
        return priority;
    }

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
