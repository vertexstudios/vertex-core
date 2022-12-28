package net.threadly.core.gui;

import org.bukkit.inventory.ItemStack;

public class GUIItem {
    private int slotNumber;

    private ItemAction action;

    private ItemStack stack;

    public GUIItem(ItemStack stack, int slotNumber, ItemAction action) {
        setItem(stack);
        this.slotNumber = slotNumber;
        this.action = action;
    }

    public void setItem(ItemStack stack) {
        this.stack = stack;
    }

    public int getSlot() {
        return this.slotNumber;
    }

    public ItemStack getItem() {
        return this.stack;
    }

    public ItemAction getAction() {
        return this.action;
    }
}
