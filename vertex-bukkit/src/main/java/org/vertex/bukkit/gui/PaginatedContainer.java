package org.vertex.bukkit.gui;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public abstract class PaginatedContainer {

    @Getter private List<Container> pages;
    @Getter private Player holder;
    @Getter private boolean loop;
    @Getter private boolean keepOpen;
    @Getter private int index = 0;
    private boolean transitioning;

    public PaginatedContainer(Player holder, boolean loop, boolean keepOpen) {
        this.holder = holder;
        this.loop = loop;
        this.transitioning = false;
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
        transitioning = true;
        holder.closeInventory();
        current.open();
    }

    public boolean isTransitioning() {
        return transitioning;
    }

    public void setTransitioning(boolean t) {
        this.transitioning = t;
    }

    public void disposeAll() {
        this.pages.forEach(Container::dispose);
    }

    public void openNextPage() {
        this.next();
        this.open();
    }

    public void openPreviousPage() {
        this.previous();
        this.open();
    }

    public abstract List<Container> buildPages();

}
