package net.threadly.core.gui.core;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface ItemAction {
    void execute(InventoryClickEvent paramInventoryClickEvent, Player paramPlayer);
}
