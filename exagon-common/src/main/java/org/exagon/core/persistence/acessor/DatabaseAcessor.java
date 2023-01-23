package org.exagon.core.persistence.acessor;

import org.exagon.core.PluginContainer;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DatabaseAcessor {
    protected Connection connection;
    protected Plugin plugin;
    protected String url;

    public DatabaseAcessor(String url) {
        this.url = url;
        this.plugin = PluginContainer.getCurrentPlugin();
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
