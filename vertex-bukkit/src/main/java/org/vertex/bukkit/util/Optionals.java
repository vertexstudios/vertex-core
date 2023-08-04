package org.vertex.bukkit.util;

import java.util.Optional;
import java.util.function.Supplier;

public class Optionals {

    public static <T> Optional<T> optionalSupplier(Supplier<T> nullableSupplier) {
        return Optional.ofNullable(nullableSupplier.get());
    }

}
