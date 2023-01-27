package org.vertex.sql;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class SQLCredentials {

    @NotNull @Getter private final String host;

    @NotNull @Getter private final String port;

    @NotNull @Getter private final String schema;

    @NotNull @Getter private final String user;

    @NotNull @Getter private final String password;
}
