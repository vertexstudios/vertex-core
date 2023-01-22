package org.exagon.core.test.impl;

import org.exagon.core.test.ITest;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;

public class Test implements ITest {
    private String id;
    private Method method;

    public Test(String id, Method method) {
        this.id = id;
        this.method = method;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Consumer<Player> getConsumer() {
        return p -> {
            try {
                method.invoke(null, p);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        };
    }

    @Override
    public void trigger(Player player) {
        getConsumer().accept(player);
    }
}
