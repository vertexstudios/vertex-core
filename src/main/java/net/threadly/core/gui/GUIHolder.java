package net.threadly.core.gui;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class GUIHolder {
    private Player holder;
    private UUID holderUUID;
    private Plugin plugin;

    public GUIHolder(Player holder, Plugin plugin) {
        this.holder = holder;
        this.plugin = plugin;
    }

    public UUID getHolderUUID() {
        return holderUUID;
    }

    public Player getHolder() {
        return holder;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void setHolder(Player holder) {
        this.holder = holder;
    }

    public void setHolderUUID(UUID holderUUID) {
        this.holderUUID = holderUUID;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }
}
