package net.threadly.core.test;

import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.function.Consumer;

public interface ITest {
    String getId();
    Method getMethod();
    Consumer<Player> getConsumer();
    void trigger(Player player);
}
