package org.vertex.bukkit.gui;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class GUIHolder {
    private Player holder;
    private UUID holderUUID;

    public GUIHolder(@NotNull Player holder) {
        this.holder = holder;
        this.holderUUID = holder.getUniqueId();
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
