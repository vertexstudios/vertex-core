package org.vertex.bukkit.subscriber;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class MultiEventSubscriber implements Subscriber, Listener, EventExecutor  {
    private Plugin plugin;
    private boolean disposed = false;
    @Getter private Multimap<Class<? extends Event>, Subscription> subscriptions = ArrayListMultimap.create();

    public MultiEventSubscriber(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        if(disposed) {
            return;
        }
        subscriptions.keys().stream()
                .filter(x -> event.getClass().isAssignableFrom(x))
                .map(subscriptions::get).findFirst().get().forEach(sub -> {
                    if(!sub.getHandled().equals(event.getClass()) && !sub.handleSubclasses()) {
                        return;
                    }
                    if(!sub.getFilters().test(event)) {
                        return;
                    }
                    sub.getHandler().accept(event);
                });
    }

    public static MultiEventSubscriber create(Plugin plugin) {
        return new MultiEventSubscriber(plugin);
    }

    public <T extends Event> MultiEventSubscriber attachSubscription(Subscription<T> subscription) {
        this.subscriptions.put(subscription.getHandled(), subscription);
        return this;
    }

    @Override
    public void dispose() {
        HandlerList.unregisterAll(this);
        this.disposed = true;
    }

    @Override
    public void subscribe() {
        this.subscriptions.values().forEach(sub ->
            Bukkit.getPluginManager().registerEvent(sub.getHandled(), this, sub.getPriority(), this, plugin, false)
        );
    }

    @Override
    public boolean disposed() {
        return disposed;
    }
}
