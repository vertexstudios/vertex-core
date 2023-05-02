package org.vertex.bukkit.gui;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class Page implements Listener {

    protected GUIHolder parent;
    protected Inventory inventory;
    protected Map<Integer, GUIItem> items = new HashMap<>();
    @Setter protected String title;

    private Rows rows;

    public Page(GUIHolder parent, String title, Rows rows) {
        this.parent = parent;
        this.title = title;
        this.inventory = Bukkit.createInventory(null, rows.slots, title);
    }

    public Page(GUIHolder parent, Rows rows) {
        this.parent = parent;
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

    public void handleClick(int slot) {

    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(!event.getClickedInventory().equals(this.inventory)) {
            return;
        }
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player)event.getWhoClicked();
            if (player.getUniqueId().equals(this.parent.getHolder().getUniqueId())) {
                event.setCancelled(true);
                if (this.items == null) {
                    return;
                }
                handleClick(event.getSlot());
                Optional<GUIItem> item = Optional.ofNullable(items.get(event.getSlot()));
                if(item.isPresent() && item.get().getItem().equals(event.getCurrentItem())) {
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
        this.items.values().forEach(item -> inventory.setItem(item.getSlot(), item.getItem()));
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

    public enum Rows {
        ONE(9),
        TWO(18),
        THREE(27),
        FOUR(36),
        FIVE(45),
        SIX(54);

        public final int slots;

        Rows(int slots) {
            this.slots = slots;
        }
    }
}
