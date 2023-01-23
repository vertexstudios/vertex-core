package org.exagon.core.persistence.acessor;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class SQLiteDBAcessor extends DatabaseAcessor {

    public SQLiteDBAcessor(File file) {
        super("jdbc:sqlite:" + file);
    }

    @Override
    public void connect() {
        try {
            plugin.getLogger().log(Level.INFO, "Connecting to the database...");
            connection = DriverManager.getConnection(url);
            plugin.getLogger().log(Level.INFO, "Connection established successfully!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            plugin.getLogger().log(Level.SEVERE, "Error while establishing database connection: ");
            plugin.getPluginLoader().disablePlugin(plugin);
        }
    }
}
