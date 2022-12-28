package net.threadly.core.sql.acessor;

import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DatabaseAcessor {
    protected Connection connection;
    protected Plugin plugin;
    protected String url;

    public DatabaseAcessor(Plugin plugin, String url) {
        this.url = url;
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public String getUrl() {
        return url;
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
