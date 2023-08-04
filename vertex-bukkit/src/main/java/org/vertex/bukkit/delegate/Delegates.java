package org.vertex.bukkit.delegate;

import java.util.Optional;
import java.util.function.Supplier;

public class Delegates {

    public static <T> Optional<T> optionalSupplier(Supplier<T> nullableSupplier) {
        return Optional.ofNullable(nullableSupplier.get());
    }

}
