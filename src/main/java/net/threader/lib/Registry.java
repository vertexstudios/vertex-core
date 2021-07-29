package net.threader.lib;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Registry<T> {

    private Map<String, T> registry = new HashMap<>();

    public <U extends T> U register(String id, U object) {
        registry.put(id, object);
        return object;
    }

    public Optional<T> find(String id) {
        return Optional.ofNullable(registry.get(id));
    }

}