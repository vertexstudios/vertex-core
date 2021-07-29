package net.threader.lib.service;

import net.threader.lib.Registry;

import java.util.Optional;

public class ServiceManager {
    private Registry<IService> registry = new Registry<>();

    public Registry<IService> getRegistry() {
        return registry;
    }

    public <T extends IService> Optional<T> getService(Class<T> clazz) {
        return registry.values().stream()
                .filter(x -> x.getClass().equals(clazz))
                .map(clazz::cast)
                .findFirst();
    }
}
