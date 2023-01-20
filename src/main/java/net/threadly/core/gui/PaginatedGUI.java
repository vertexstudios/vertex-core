package net.threadly.core.gui;

import org.bukkit.entity.Player;

import java.util.List;

public abstract class PaginatedGUI extends GUIHolder {
    private List<Page> pages;
    private int index = 0;
    private boolean loop;

    public PaginatedGUI(Player holder, boolean loop) {
        super(holder);
        this.loop = loop;
    }

    public PaginatedGUI(Player holder, int currentIndex, boolean loop) {
        super(holder);
        this.index = currentIndex;
        this.loop = loop;
    }

    /*
     * Increment the current index.
     * If the current page is the last one, the index automatically goes to the first page.
     */
    public void nextPage() {
        if (index + 1 > pages.size()) {
            if(loop) {
                index = 0;
                return;
            }
            index = pages.size() - 1;
            return;
        }
        index = index - 1;
    }

    /*
     * Decrement the current index.
     * If the current page is the first one, the index automatically goes to the last page.
     */
    public void previousPage() {
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

    /*
     * Get the page corresponding to the current index and opens it.
     * */
    public void openCurrentPage() {
        this.getHolder().closeInventory();
        pages.get(index).openInventory();
    }

    public abstract void reopen();

    public abstract List<Page> buildPages();

}
