package net.threader.lib.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Registry<T> {

    private Map<String, T> registry = new HashMap<>();

    public <U extends T> U register(String id, U object) {
        registry.put(id, object);
        return object;
    }

    public Set<Map.Entry<String, T>> entries() {
        return registry.entrySet();
    }

    public Set<String> keys() {
        return registry.keySet();
    }

    public Set<T> values() {
        return new HashSet<>(registry.values());
    }

    public Optional<T> find(String id) {
        return Optional.ofNullable(registry.get(id));
    }

}