package org.vertex.bukkit.gui;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Builder

public class ContainerElement {
    @Getter private int slot;
    @Getter private ElementAction action;
    @Getter private ItemStack stack;
}
