package net.threadly.core.service;

import net.threadly.core.util.Registry;

import java.util.Optional;

public class ServiceManager {
    private Registry<Class<?>, IService> registry = new Registry<>();

    public Registry<Class<?>, IService> getRegistry() {
        return registry;
    }

    public <T extends IService> Optional<T> getService(Class<T> clazz) {
        return (Optional<T>) registry.find(clazz);
    }
}
