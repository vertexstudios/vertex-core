package org.bitstudio.core.gui;

import org.bitstudio.core.PluginContainer;
import org.bitstudio.core.gui.core.GUIItem;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class Page implements Listener {

    protected GUIHolder parent;
    protected Inventory inventory;
    protected Map<Integer, GUIItem> items = new HashMap<>();
    protected String title;

    public Page(GUIHolder parent, String title, Rows rows) {
        this.parent = parent;
        this.title = title;
        this.build().forEach(item -> items.put(item.getSlot(), item));
        this.inventory = Bukkit.createInventory(null, rows.slots, title);
    }

    public abstract List<GUIItem> build();

    public void openInventory() {
        putItems();
        if(this.parent.getHolder() == null) {
            return;
        }
        this.parent.getHolder().openInventory(this.inventory);
        try {
            Bukkit.getPluginManager().registerEvents(this, PluginContainer.getCurrentPlugin());
        } catch (Exception e) {
            this.parent.getHolder().closeInventory();
            this.parent.getHolder().sendMessage("Error while trying to register the inventory.");
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player)event.getWhoClicked();
            if (player.getName().equals(this.parent.getHolder().getName())) {
                event.setCancelled(true);
                if (this.items == null) {
                    return;
                }
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
        if (event.getPlugin() != PluginContainer.getCurrentPlugin())
            return;
        Player player = Bukkit.getPlayer(this.parent.getHolderUUID());
        if (player != null)
            player.closeInventory();
    }

    private void putItems() {
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
