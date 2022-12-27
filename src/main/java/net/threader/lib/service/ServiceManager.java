package net.threader.lib.service;

import net.threader.lib.util.Registry;

import java.util.Optional;

public class ServiceManager {
    private Registry<Class<?>, IService> registry = new Registry<>();

    public Registry<Class<?>, IService> getRegistry() {
        return registry;
    }

    public <T extends IService> Optional<T> getService(Class<T> clazz) {
        return registry.values().stream()
                .filter(x -> x.getClass().isAssignableFrom(clazz))
                .map(clazz::cast)
                .findFirst();
    }
}
