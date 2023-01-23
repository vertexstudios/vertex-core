package org.exagon.sql;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class SQLCredentials {

    @NotNull @Getter private final String host;

    @NotNull @Getter private final String port;

    @NotNull @Getter private final String schema;

    @NotNull @Getter private final String user;

    @NotNull @Getter private final String password;

    public static SQLCredentials from(@NotNull ConfigurationSection section) {
        return new SQLCredentials(
                section.getString("host"),
                section.getString("port"),
                section.getString("schema"),
                section.getString("user"),
                section.getString("password")
        );
    }
}
