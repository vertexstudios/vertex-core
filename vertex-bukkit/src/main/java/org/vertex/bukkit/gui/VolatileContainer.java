package org.vertex.bukkit.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Map;
import java.util.function.Consumer;

// Volatile containers are used to create non static inventories that can be changed on the fly.
public abstract class VolatileContainer extends Container {


    public VolatileContainer(Player holder, String title, Rows rows, PaginatedContainer parent) {
        super(holder, title, rows, parent);
    }

    public VolatileContainer(Player holder, String title, Rows rows) {
        super(holder, title, rows);
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void setItems(Map<Integer, ContainerElement> items) {
        this.items = items;
    }
}
