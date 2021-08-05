package net.threader.lib;

import net.threader.lib.service.ServiceManager;
import net.threader.lib.test.TestService;
import org.bukkit.plugin.java.JavaPlugin;

public class ThreaderLib extends JavaPlugin {
    private static ThreaderLib instance;
    private ServiceManager serviceManager;

    @Override
    public void onEnable() {
        instance = this;
        serviceManager.getRegistry().register(TestService.class, new TestService());
    }

    public static ThreaderLib instance() {
        return instance;
    }
}
