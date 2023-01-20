package net.threadly.core.gui;

import org.bukkit.entity.Player;

import java.util.UUID;

public class GUIHolder {
    private Player holder;
    private UUID holderUUID;

    public GUIHolder(Player holder) {
        this.holder = holder;
    }

    public UUID getHolderUUID() {
        return holderUUID;
    }

    public Player getHolder() {
        return holder;
    }


    public void setHolder(Player holder) {
        this.holder = holder;
    }

    public void setHolderUUID(UUID holderUUID) {
        this.holderUUID = holderUUID;
    }
}
