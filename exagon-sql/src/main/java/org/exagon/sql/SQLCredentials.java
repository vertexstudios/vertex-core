package org.exagon.sql;

import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

public class SQLCredentials {

    @NotNull @Getter private String host;

    @NotNull @Getter private String port;

    @NotNull @Getter private String schema;

    @NotNull @Getter private String user;

    @NotNull @Getter private String password;

}
