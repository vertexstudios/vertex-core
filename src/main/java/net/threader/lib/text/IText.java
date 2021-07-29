package net.threader.lib.text;

import org.bukkit.entity.Player;

import java.util.List;

public interface IText {
    List<Object> getObjects();
    void send(Player player);
}
