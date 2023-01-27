package org.vertex.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public class BukkitPluginContainer {
    private static JavaPlugin currentPlugin;

    public static JavaPlugin getCurrentPlugin() {
        return currentPlugin;
    }

    public static void setCurrentPlugin(JavaPlugin plugin) {
        currentPlugin = plugin;
    }
}
