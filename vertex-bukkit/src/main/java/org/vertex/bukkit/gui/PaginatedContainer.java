package org.vertex.bukkit.gui;

import lombok.Getter;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.vertex.bukkit.protocol.Protocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PaginatedContainer {

    @Getter private List<Container> pages;
    @Getter private Player holder;
    @Getter private boolean loop;
    @Getter private boolean keepOpen;
    @Getter private int index = 0;

    public PaginatedContainer(Player holder, boolean loop, boolean keepOpen) {
        this.holder = holder;
        this.loop = loop;
        this.keepOpen = keepOpen;
    }

    public PaginatedContainer(Player holder, boolean loop, boolean keepOpen, int index) {
        this(holder,loop,keepOpen);
        this.index = index;
    }

    public void next() {
        if (index + 1 >= pages.size()) {
            if(loop) {
                index = 0;
                return;
            }
            index = pages.size() - 1;
            return;
        }
        index = index + 1;
    }

    public void previous() {
        if (index - 1 < 0) {
            if(loop) {
                index = pages.size() - 1;
                return;
            }
            index = 0;
            return;
        }
        index = index - 1;
    }

    public Container getCurrentPage() {
        return pages.get(index);
    }

    public void open() {
        Container current = getCurrentPage();
        if(keepOpen && current instanceof VolatileContainer) {

            VolatileContainer volatileContainer = (VolatileContainer) current;

            Inventory currentInventory = holder.getOpenInventory().getTopInventory();
            volatileContainer.update(currentInventory);

            holder.updateInventory();
            return;
        }
        this.holder.closeInventory();
        this.holder.openInventory(current.getInventory());
    }

    public abstract List<Container> buildPages();

}
