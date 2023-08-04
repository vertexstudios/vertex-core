package org.vertex.bukkit.gui;

import net.minecraft.server.level.EntityPlayer;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.vertex.bukkit.protocol.Protocol;

import java.util.HashMap;
import java.util.Map;

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

    public void update(Inventory inventory) {

        this.inventory.clear();

        Map<Integer, ContainerElement> elements = new HashMap<>();

        this.build().forEach(x -> {
            elements.put(x.getSlot(), x);
            inventory.setItem(x.getSlot(), x.getStack());
        });

        EntityPlayer ep = ((CraftPlayer)holder).getHandle();
        Protocol.UpdateScreen.builder()
                .containerId(ep.bR.j)
                .title(this.getTitle())
                .windowType(this.getRows().rowsNumber - 1)
                .build().send(holder);

    }
}
