package net.threader.lib.gui;

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
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InventoryGUI implements Listener {
    private String playerName;

    private Inventory inventory;

    private List<GUIItem> items;

    private Plugin plugin;

    public InventoryGUI(Plugin plugin, Player player, String chestName, Rows rows, List<ItemStack> items) {
        this(plugin, player, chestName, rows, createGuiItemsFromStacks(items));
    }

    public InventoryGUI(Plugin plugin, Player player, String chestName, Rows rows, GUIItem... items) {
        this.plugin = plugin;
        this.playerName = player.getName();
        this.inventory = Bukkit.createInventory(null, rows.slots, chestName);
        this.items = new ArrayList<>(Arrays.asList(items));
    }

    public void addItem(GUIItem item) {
        this.items.add(item);
    }

    private static GUIItem[] createGuiItemsFromStacks(List<ItemStack> stacks) {
        GUIItem[] guiItems = new GUIItem[stacks.size()];
        for (int i = 0; i < stacks.size(); i++)
            guiItems[i] = new GUIItem(stacks.get(i), i, null);
        return guiItems;
    }

    public void openInventory(Player player) {
        putItems();
        player.openInventory(this.inventory);
        try {
            Bukkit.getPluginManager().registerEvents(this, this.plugin);
        } catch (Exception e) {
            player.closeInventory();
            player.sendMessage("was an error when trying to register the inventory.");
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player)event.getWhoClicked();
            if (player.getName().equals(this.playerName)) {
                event.setCancelled(true);
                if (this.items == null)
                    return;
                for (GUIItem item : this.items) {
                    if (item.getSlot() == event.getSlot() && item.getItem().equals(event.getCurrentItem())) {
                        ItemAction action = item.getAction();
                        if (action != null)
                            action.execute(event, player);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getPlayer().getName().equals(this.playerName))
            destroyListeners();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (event.getPlayer().getName().equals(this.playerName))
            destroyListeners();
    }

    @EventHandler
    public void pluginDisable(PluginDisableEvent event) {
        if (event.getPlugin() != this.plugin)
            return;
        Player player = Bukkit.getPlayerExact(this.playerName);
        if (player != null)
            player.closeInventory();
    }

    private void putItems() {
        this.inventory.clear();
        for (GUIItem guiItem : this.items)
            this.inventory.setItem(guiItem.getSlot(), guiItem.getItem());
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void destroyListeners() {
        HandlerList.unregisterAll(this);
        this.playerName = null;
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
