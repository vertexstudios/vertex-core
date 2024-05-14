package org.vertex.bukkit.gui;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.Nullable;
import org.vertex.bukkit.BukkitPluginContainer;
import org.vertex.bukkit.delegate.Delegates;
import org.vertex.bukkit.pipeline.ConsumerPipeline;
import org.vertex.bukkit.subscriber.MultiEventSubscriber;
import org.vertex.bukkit.subscriber.Subscription;

import java.util.*;
import java.util.function.Consumer;

public abstract class Container {

    @Getter
    @NonNull
    protected Inventory inventory;
    @Getter
    @NonNull
    protected Player holder;
    @Getter
    @NonNull
    protected Map<Integer, ContainerElement> items;
    @Getter
    @Setter
    @NonNull
    protected String title;
    @Getter
    @NonNull
    protected Rows rows;

    @Getter
    @NonNull
    protected Map<Integer, Consumer<InventoryClickEvent>> topInventoryActions;

    @Getter
    @NonNull
    protected Map<Integer, Consumer<InventoryClickEvent>> bottomInventoryActions;

    @Getter
    @NonNull
    protected ConsumerPipeline<Container> closingPipeline;
    @Getter
    @NonNull
    protected ConsumerPipeline<Container> openningPipeline;

    @Getter
    @NonNull
    protected MultiEventSubscriber subscriber;

    @Nullable
    protected PaginatedContainer parent;

    public Container(Player holder, String title, Rows rows, PaginatedContainer parent) {
        this(holder, title, rows);
        this.parent = parent;
    }

    public Container(Player holder, String title, Rows rows) {
        this.holder = holder;
        this.items = new HashMap<>();
        this.title = title;
        this.rows = rows;
        this.inventory = Bukkit.createInventory(null, rows.slots, title);

        this.closingPipeline = new ConsumerPipeline<>();
        this.openningPipeline = new ConsumerPipeline<>();

        this.topInventoryActions = new HashMap<>();
        this.bottomInventoryActions = new HashMap<>();

        this.subscriber = MultiEventSubscriber.create(BukkitPluginContainer.getCurrentPlugin());

        // PlayerQuitEvent
        this.subscriber.attachSubscription(Subscription.create(PlayerQuitEvent.class)
                .withFilter(e -> e.getPlayer().equals(holder))
                .handler(x -> {
                    this.dispose();
                    if (this.hasParentContainer()) {
                        this.getParent().get().getPages().forEach(Container::dispose);
                    }
                }));

        // InventoryCloseEvent
        this.subscriber.attachSubscription(Subscription.create(InventoryCloseEvent.class)
                .withFilter(e -> e.getPlayer().equals(holder))
                .handler(x -> {
                    this.dispose();
                    if (this.hasParentContainer()) {
                        this.getParent().get().getPages().forEach(Container::dispose);
                    }
                }));

        // PluginDisabledEvent
        this.subscriber.attachSubscription(Subscription.create(PluginDisableEvent.class)
                .withFilter(e -> e.getPlugin() == BukkitPluginContainer.getCurrentPlugin())
                .handler(e -> {
                    Player p = Bukkit.getPlayer(holder.getUniqueId());
                    if (p != null) {
                        p.closeInventory();
                    }
                    this.dispose();
                    if (this.hasParentContainer()) {
                        this.getParent().get().getPages().forEach(Container::dispose);
                    }
                }));

        // InventoryClickEvent
        this.subscriber.attachSubscription(Subscription.create(InventoryClickEvent.class)
                .withFilter(e -> e.getClickedInventory() != null)
                .withFilter(e -> holder.getOpenInventory() != null)
                .withFilter(e -> e.getView().getTopInventory().equals(this.inventory))
                .withFilter(e -> Objects.equals(e.getClickedInventory(), e.getView().getBottomInventory()) || e.getClickedInventory().equals(e.getView().getTopInventory()))
                .withFilter(e -> e.getWhoClicked() instanceof Player)
                .withFilter(e -> e.getWhoClicked().getUniqueId().equals(holder.getUniqueId()))
                .withFilter(e -> this.items != null)
                .handler(event -> {

                    InventoryView view = holder.getOpenInventory();
                    Inventory clicked = event.getClickedInventory();
                    int slot = event.getSlot();
                    event.setCancelled(true);

                    if(clicked.equals(view.getBottomInventory())) {
                        Delegates.optionalSupplier(() -> this.bottomInventoryActions.get(slot))
                                .ifPresent(x -> x.accept(event));
                        Delegates.optionalSupplier(() -> this.bottomInventoryActions.get(Slots.ANY))
                                .ifPresent(x -> x.accept(event));
                    }

                    if(clicked.equals(view.getTopInventory())) {
                        Delegates.optionalSupplier(() -> this.topInventoryActions.get(slot))
                                .ifPresent(x -> x.accept(event));
                        Delegates.optionalSupplier(() -> this.topInventoryActions.get(Slots.ANY))
                                .ifPresent(x -> x.accept(event));
                    }

                    Optional<ContainerElement> item = Optional.ofNullable(items.get(slot));
                    if (item.isPresent() && item.get().getStack().equals(event.getCurrentItem())) {
                        Optional.ofNullable(item.get().getAction())
                                .ifPresent(action -> action.click(holder, event));
                    }

                })
        );

        this.subscriber.subscribe();
    }

    public void attachCustomActions(ElementAction.Restrictiveness restrictiveness, Consumer<InventoryClickEvent> action, int... slots) {
        for (int slot : slots) {
            switch (restrictiveness) {
                case TOP_ONLY -> this.topInventoryActions.put(slot, action);
                case BOTTOM_ONLY -> this.bottomInventoryActions.put(slot, action);
                case BOTH -> {
                    this.bottomInventoryActions.put(slot, action);
                    this.topInventoryActions.put(slot, action);
                }
            }
        }
    }

    public boolean hasParentContainer() {
        return parent != null;
    }

    public Optional<PaginatedContainer> getParent() {
        return Optional.ofNullable(parent);
    }

    public void open() {
        if (holder == null) {
            return;
        }
        if (this.inventory == null) {
            this.inventory = Bukkit.createInventory(null, this.rows.slots, this.title);
        }
        this.placeElements();
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

    public enum Rows {
        ONE(9, 1),
        TWO(18, 2),
        THREE(27, 3),
        FOUR(36, 4),
        FIVE(45, 5),
        SIX(54, 6);

        public final int slots;

        public final int rowsNumber;


        Rows(int slots, int rowsNumber) {
            this.slots = slots;
            this.rowsNumber = rowsNumber;
        }

        public int getRowsNumber() {
            return rowsNumber;
        }

        public static Rows byRowsNumber(int number) {
            return Arrays.stream(values()).filter(x -> x.rowsNumber == number).findFirst().get();
        }

    }

}
