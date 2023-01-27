package org.vertex.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class MongoCredentials {

    @NotNull @Getter private final String uri;

    @NotNull @Getter private final String database;

    public static MongoCredentials from(@NotNull String uri, @NotNull String database) {
        return new MongoCredentials(uri, database);
    }
}
