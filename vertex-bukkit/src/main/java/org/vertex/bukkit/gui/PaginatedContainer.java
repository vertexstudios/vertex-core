package org.vertex.bukkit.gui;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public abstract class PaginatedContainer {

    private List<Container> pages;
    private Player holder;
    private boolean loop;
    private boolean keepOpen;
    private int index = 0;

    public PaginatedContainer(Player holder, boolean loop, boolean keepOpen) {
        this.holder = holder;
        this.loop = loop;
        this.keepOpen = keepOpen;
        this.pages = buildPages();
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

    public List<Container> getPages() {
        return pages;
    }

    public int getIndex() {
        return index;
    }

    public Player getHolder() {
        return holder;
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
        this.holder.openInventory(current.inventory);
    }

    public abstract List<Container> buildPages();

}
