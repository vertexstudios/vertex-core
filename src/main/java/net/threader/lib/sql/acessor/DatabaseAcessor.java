package net.threader.lib.sql.acessor;

import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DatabaseAcessor {
    protected Connection connection;
    protected Plugin plugin;
    protected String url;

    public DatabaseAcessor(String url) {
        this.url = url;
    }

    public Connection getConnection() {
        try {
            if(connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }

    public abstract void connect();
}
