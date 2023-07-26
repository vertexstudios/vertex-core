package org.vertex.bukkit.gui;

import org.bukkit.inventory.ItemStack;

import com.google.common.base.Supplier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder

public class ContainerElement {
    @Getter private int slot;
    @Getter private ElementAction action;
    @Getter private ItemStack stack;
}
