package org.vertex.bukkit.gui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.vertex.bukkit.BukkitPluginContainer;
import org.vertex.bukkit.gui.Page.Rows;
import org.vertex.bukkit.gui.pipeline.ContainerClosingPipeline;
import org.vertex.bukkit.gui.pipeline.ContainerPipeline;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

public class Container implements Listener {
    
    @Getter @NonNull private Inventory inventory;
    @Getter @NonNull private Player holder;
    @Getter @NonNull private Map<Integer, ContainerElement> items;
    @Getter @NonNull private String title;

    @Getter @NonNull private ContainerPipeline closingPipeline;
    @Getter @NonNull private ContainerPipeline openningPipeline;

    public Container(Player holder, String title, Rows rows) {
        this.holder = holder;
        this.items = new HashMap<>();
        this.title = title;
        this.inventory = Bukkit.createInventory(null, rows.slots, title);
        Bukkit.getPluginManager().registerEvents(this, BukkitPluginContainer.getCurrentPlugin());

        this.closingPipeline = ContainerClosingPipeline.create(); 
    }

    public void dispose() {
        HandlerList.unregisterAll(this);
        this.inventory = null;
        this.holder = null;
        this.items = null;
    }

    @AllArgsConstructor
    public enum Rows {
        ONE(9, 1),
        TWO(18, 2),
        THREE(27, 3),
        FOUR(36, 4),
        FIVE(45, 5),
        SIX(54, 6);

        public int slots;

        @Getter
        public int rowsNumber;

        public static Rows byRowsNumber(int number) {
            return Arrays.stream(values()).filter(x -> x.rowsNumber == number).findFirst().get();
        }

    }

}
