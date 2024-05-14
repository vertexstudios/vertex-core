package org.vertex.bukkit.subscriber;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class SimpleEventSubscriber<T extends Event> implements Subscriber {

    @Getter private Subscription<T> subscription;
    private boolean disposed = false;
    @Getter private Plugin plugin;

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        if(disposed) {
            event.getHandlers().unregister(listener);
            return;
        }
        if(!subscription.getHandled().equals(event.getClass()) && !subscription.handleSubclasses()) {
            return;
        }
        if(!subscription.getHandled().isInstance(event)){
            return;
        }
        T casted = (T) event;
        if(!subscription.getFilters().test(casted)) {
            return;
        }
        subscription.getHandler().accept(casted);
    }

    public SimpleEventSubscriber(Plugin plugin) {
        this.plugin = plugin;
    }

    public static SimpleEventSubscriber create(Plugin plugin) {
        return new SimpleEventSubscriber(plugin);
    }

    @Override
    public void dispose() {
        HandlerList.unregisterAll(this);
        this.disposed = true;
    }

    @Override
    public void subscribe() {
        Bukkit.getPluginManager().registerEvent(this.subscription.getHandled(), this, this.subscription.getPriority(), this, plugin, false);
    }

    @Override
    public boolean disposed() {
        return disposed;
    }

}
