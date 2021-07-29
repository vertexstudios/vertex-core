package net.threader.lib.testing;

import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.function.Consumer;

public interface Test {
    String getId();
    Method getMethod();
    Consumer<Player> getConsumer();
    void trigger(Player player);
}
