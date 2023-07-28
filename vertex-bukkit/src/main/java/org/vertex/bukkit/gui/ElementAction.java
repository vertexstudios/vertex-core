package org.vertex.bukkit.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface ElementAction {
    void click(Player player, InventoryClickEvent event);

    enum Restrictiveness {
        TOP_ONLY,
        BOTTOM_ONLY,
        BOTH;
    }
}
