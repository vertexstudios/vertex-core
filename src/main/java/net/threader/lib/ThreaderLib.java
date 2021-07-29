package net.threader.lib;

import org.bukkit.plugin.java.JavaPlugin;

public class ThreaderLib extends JavaPlugin {
    private static ThreaderLib instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    public static ThreaderLib instance() {
        return instance;
    }
}
