package net.threader.lib.gui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
