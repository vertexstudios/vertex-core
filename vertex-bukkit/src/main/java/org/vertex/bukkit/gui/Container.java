package org.vertex.bukkit.gui;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.vertex.bukkit.BukkitPluginContainer;
import org.vertex.bukkit.subscriber.MultiEventSubscriber;
import org.vertex.bukkit.subscriber.Subscription;
import org.vertex.bukkit.pipeline.ConsumerPipeline;

import java.util.*;
import java.util.function.Consumer;

public abstract class Container {
    
    @Getter @NonNull private Inventory inventory;
    @Getter @NonNull private Player holder;
    @Getter @NonNull private Map<Integer, ContainerElement> items;
    @Getter @NonNull private String title;
    @Getter @NonNull private Rows rows;

    @Getter @NonNull private Map<Integer, Consumer<InventoryClickEvent>> customActions;

    @Getter @NonNull private ConsumerPipeline<Container> closingPipeline;
    @Getter @NonNull private ConsumerPipeline<Container> openningPipeline;

    @Getter @NonNull private MultiEventSubscriber subscriber;

    public Container(Player holder, String title, Rows rows) {
        this.holder = holder;
        this.items = new HashMap<>();
        this.title = title;
        this.rows = rows;
        this.inventory = Bukkit.createInventory(null, rows.slots, title);
        this.closingPipeline = new ConsumerPipeline<>();
        this.openningPipeline = new ConsumerPipeline<>();
        this.customActions = new HashMap<>();

        this.subscriber = MultiEventSubscriber.create(BukkitPluginContainer.getCurrentPlugin());

        // PlayerQuitEvent
        this.subscriber.attachSubscription(Subscription.create(PlayerQuitEvent.class)
                .withFilter(e -> e.getPlayer().equals(holder))
                .handler(x -> this.dispose()));

        // InventoryCloseEvent
        this.subscriber.attachSubscription(Subscription.create(InventoryCloseEvent.class)
                .withFilter(e -> e.getPlayer().equals(holder))
                .handler(x -> this.dispose()));

        // PluginDisabledEvent
        this.subscriber.attachSubscription(Subscription.create(PluginDisableEvent.class)
                .withFilter(e -> e.getPlugin() == BukkitPluginContainer.getCurrentPlugin())
                .handler(e -> {
                    Player p = Bukkit.getPlayer(holder.getUniqueId());
                    if(p != null) {
                        p.closeInventory();
                    }
                    this.dispose();
                }));

        // InventoryClickEvent
        this.subscriber.attachSubscription(Subscription.create(InventoryClickEvent.class)
                .withFilter(e -> e.getClickedInventory() != null)
                .withFilter(e -> e.getInventory().equals(this.inventory))
                .withFilter(e -> e.getWhoClicked() instanceof Player)
                .withFilter(e -> e.getWhoClicked().getUniqueId().equals(holder.getUniqueId()))
                .withFilter(e -> this.items != null)
                .handler(event -> {
                    event.setCancelled(true);

                    if(this.customActions.get(event.getSlot()) != null) {
                        this.customActions.get(event.getSlot()).accept(event);
                    }

                    Optional<ContainerElement> item = Optional.ofNullable(items.get(event.getSlot()));
                    if (item.isPresent() && item.get().getStack().equals(event.getCurrentItem())) {
                        Optional.ofNullable(item.get().getAction()).ifPresent(action -> action.click(holder, event));
                    }
                })
        );

        this.subscriber.subscribe();
    }

    public void attachCustomActions(Consumer<InventoryClickEvent> action, int... slots) {
        for(int i = 0; i < slots.length; i++) {
            this.customActions.put(slots[i], action);
        }
    }

    public void open() {
        if (holder == null) {
            return;
        }
        if (this.inventory == null) {
            this.inventory = Bukkit.createInventory(null, this.rows.slots, this.title);
        }
        this.placeElements();
        holder.closeInventory();
        holder.openInventory(this.inventory);
    }

    private void placeElements() {
        this.build().forEach(item -> items.put(item.getSlot(), item));
        this.inventory.clear();
        if (this.items != null) {
            this.items.values().forEach(item -> inventory.setItem(item.getSlot(), item.getStack()));
        }
    }

    public void dispose() {
        this.subscriber.dispose();
        this.inventory = null;
        this.holder = null;
        this.items = null;
    }

    public abstract List<ContainerElement> build();

    @AllArgsConstructor
    public enum Rows {
        ONE(9, 1),
        TWO(18, 2),
        THREE(27, 3),
        FOUR(36, 4),
        FIVE(45, 5),
        SIX(54, 6);

        public int slots;

        @Getter
        public int rowsNumber;

        public static Rows byRowsNumber(int number) {
            return Arrays.stream(values()).filter(x -> x.rowsNumber == number).findFirst().get();
        }

    }

}
