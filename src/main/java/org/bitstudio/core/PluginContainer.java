package org.bitstudio.core;

import org.bukkit.plugin.java.JavaPlugin;

public class PluginContainer {
    private static JavaPlugin currentPlugin;

    public static JavaPlugin getCurrentPlugin() {
        return currentPlugin;
    }

    public static void setCurrentPlugin(JavaPlugin plugin) {
        PluginContainer.currentPlugin = plugin;
    }
}
