package org.vertex.bukkit.gui;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.vertex.bukkit.BukkitPluginContainer;
import org.vertex.bukkit.gui.core.GUIItem;

import java.util.*;

public abstract class Page implements Listener {

    protected GUIHolder parent;
    protected Inventory inventory;
    protected Map<Integer, GUIItem> items = new HashMap<>();
    @Getter @Setter protected String title;

    private Rows rows;

    public Page(GUIHolder parent, String title, Rows rows) {
        this.parent = parent;
        this.items = new HashMap<>();
        this.title = title;
        this.inventory = Bukkit.createInventory(null, rows.slots, title);
    }

    public Page(GUIHolder parent, Rows rows) {
        this.parent = parent;
        this.items = new HashMap<>();
        this.rows = rows;
    }

    public abstract List<GUIItem> build();

    public void openInventory() {
        if(this.parent.getHolder() == null) {
            return;
        }
        if (this.inventory == null) {
            this.inventory = Bukkit.createInventory(null, this.rows.slots, this.title);
        }
        putItems();
        this.parent.getHolder().closeInventory();
        this.parent.getHolder().openInventory(this.inventory);
        try {
            Bukkit.getPluginManager().registerEvents(this, BukkitPluginContainer.getCurrentPlugin());
        } catch (Exception e) {
            this.parent.getHolder().closeInventory();
            this.parent.getHolder().sendMessage("Error while trying to register the inventory.");
            e.printStackTrace();
        }
    }

    public void handleClick(InventoryClickEvent event) {

    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        System.out.println("1");
        if(event.getClickedInventory() == null) {
            System.out.println("2");
            return;
        }
        if(!event.getInventory().equals(this.inventory)) {
            System.out.println("3");
            return;
        }
        if (event.getWhoClicked() instanceof Player) {
            System.out.println("4");
            Player player = (Player) event.getWhoClicked();
            if (player.getUniqueId().equals(this.parent.getHolder().getUniqueId())) {
                System.out.println("5");
                event.setCancelled(true);
                if (this.items == null) {
                    System.out.println("6");
                    return;
                }
                handleClick(event);
                Optional<GUIItem> item = Optional.ofNullable(items.get(event.getSlot()));
                if(item.isPresent() && item.get().getItem().equals(event.getCurrentItem())) {
                    System.out.println("7");
                    Optional.ofNullable(item.get().getAction()).ifPresent(action -> action.execute(event, player));
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getPlayer().getUniqueId().equals(this.parent.getHolderUUID()))
            destroyListeners();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (event.getPlayer().getUniqueId().equals(this.parent.getHolderUUID()))
            destroyListeners();
    }

    @EventHandler
    public void pluginDisable(PluginDisableEvent event) {
        if (event.getPlugin() != BukkitPluginContainer.getCurrentPlugin())
            return;
        Player player = Bukkit.getPlayer(this.parent.getHolderUUID());
        if (player != null)
            player.closeInventory();
    }

    private void putItems() {
        this.build().forEach(item -> items.put(item.getSlot(), item));
        this.inventory.clear();
        if(this.items != null) {
            this.items.values().forEach(item -> inventory.setItem(item.getSlot(), item.getItem()));
        }
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void destroyListeners() {
        HandlerList.unregisterAll(this);
        this.parent = null;
        this.inventory = null;
        this.items = null;
    }

    @AllArgsConstructor
    public enum Rows {
        ONE(9, 1),
        TWO(18, 2),
        THREE(27, 3),
        FOUR(36, 4),
        FIVE(45, 5),
        SIX(54, 6);

        public int slots;

        @Getter public int rowsNumber;

        public static Rows byRowsNumber(int number) {
            return Arrays.stream(values()).filter(x -> x.rowsNumber == number).findFirst().get();
        }

    }
}
