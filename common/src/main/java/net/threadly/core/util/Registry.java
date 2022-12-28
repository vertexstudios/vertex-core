package net.threadly.core.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Registry<T, U> {

    private Map<T, U> registry = new HashMap<>();

    public <V extends U> V register(T id, U object) {
        registry.put(id, object);
        return (V) object;
    }

    public Set<Map.Entry<T, U>> entries() {
        return registry.entrySet();
    }

    public Set<T> keys() {
        return registry.keySet();
    }

    public Set<U> values() {
        return new HashSet<>(registry.values());
    }

    public Optional<U> find(T id) {
        return Optional.ofNullable(registry.get(id));
    }

    public void remove(T id) {
        this.find(id).ifPresent(x -> registry.remove(id));
    }
}
