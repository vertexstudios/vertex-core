package org.vertex.bukkit.gui;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.vertex.bukkit.gui.pipeline.ContainerPipeline;

import lombok.Getter;
import lombok.NonNull;

public class Container implements Listener {
    
    @Getter @NonNull private Inventory inventory;
    @Getter @NonNull private Player holder;
    @Getter @NonNull private Map<Integer, ContainerElement> items;

    @Getter @NonNull private ContainerPipeline closingPipeline;
    @Getter @NonNull private ContainerPipeline openningPipeline;

    public void dispose() {
        HandlerList.unregisterAll(this);
        this.inventory = null;
        this.holder = null;
        this.items = null;
    }

}
