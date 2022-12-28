package net.threadly.core.text;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;

import java.util.List;

public interface IText {
    List<Object> getObjects();
    BaseComponent[] build();
    void send(Player player);
}
