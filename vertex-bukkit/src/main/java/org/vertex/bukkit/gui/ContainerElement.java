package org.vertex.bukkit.gui;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Builder

public class ContainerElement {
    private int slot;
    private ElementAction action;
    private ItemStack stack;

    public ContainerElement(int slot, ElementAction action, ItemStack stack) {
        this.slot = slot;
        this.action = action;
        this.stack = stack;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getStack() {
        return stack;
    }

    public ElementAction getAction() {
        return action;
    }
}
