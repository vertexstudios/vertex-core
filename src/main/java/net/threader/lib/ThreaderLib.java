package net.threader.lib;

import net.threader.lib.service.ServiceManager;
import net.threader.lib.test.TestService;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

public class ThreaderLib extends JavaPlugin implements Listener {
    private static ThreaderLib instance;
    private ServiceManager serviceManager;

    @Override
    public void onEnable() {
        instance = this;
        serviceManager = new ServiceManager();
        serviceManager.getRegistry().register(TestService.class, new TestService());
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    public static ThreaderLib instance() {
        return instance;
    }

}
