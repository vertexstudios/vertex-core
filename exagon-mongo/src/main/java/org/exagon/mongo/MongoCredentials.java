package org.exagon.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class MongoCredentials {

    @NotNull @Getter private final String uri;

    @NotNull @Getter private final String database;

    public static MongoCredentials from(@NotNull ConfigurationSection section) {
        return new MongoCredentials(section.getString("uri"), section.getString("database"));
    }
}
