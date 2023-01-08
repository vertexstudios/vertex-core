package net.threadly.core;

import net.threadly.core.conversor.TypeConversor;
import net.threadly.core.service.ICommandService;
import net.threadly.core.service.ServiceManager;
import net.threadly.core.service.impl.DefaultCommandService;
import net.threadly.core.util.Registry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public abstract class PluginBase extends JavaPlugin {

    protected ServiceManager serviceManager;

    protected Registry<String, TypeConversor<?,?>> conversors;

    @Override
    public void onEnable() {
        this.serviceManager = new ServiceManager();

        this.serviceManager.getRegistry().register(ICommandService.class, new DefaultCommandService());

        conversors = new Registry<>();

        conversors.register("string-to-int", ((t) -> Integer.parseInt((String) t)));
        conversors.register("string-to-boolean", ((t) -> Boolean.parseBoolean((String) t)));
        conversors.register("name-to-player", ((t) -> Bukkit.getPlayer((String) t)));
        conversors.register("uuid-to-player", ((t) -> Bukkit.getPlayer((UUID) t)));

        init();

        this.serviceManager.getService(ICommandService.class).get().register(this, conversors);
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    public Registry<String, TypeConversor<?, ?>> getConversors() {
        return conversors;
    }

    public void init() {

    }

}
