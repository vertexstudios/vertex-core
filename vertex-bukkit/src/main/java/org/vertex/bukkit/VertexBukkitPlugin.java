package org.vertex.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.exagon.core.conversor.TypeConversor;
import org.exagon.core.service.ICommandService;
import org.exagon.core.service.ServiceManager;
import org.exagon.core.util.Registry;
import org.vertex.bukkit.service.DefaultCommandService;

import java.util.UUID;

public class VertexBukkitPlugin extends JavaPlugin {

    protected ServiceManager serviceManager;

    protected Registry<String, TypeConversor<?,?>> conversors;
    @Override
    public void onLoad() {
        BukkitPluginContainer.setCurrentPlugin(this);
    }

    @Override
    public void onEnable() {
        this.serviceManager = new ServiceManager();
        this.serviceManager.getRegistry().register(ICommandService.class, new DefaultCommandService());

        conversors = new Registry<>();

        conversors.register("string-to-int", ((t) -> Integer.parseInt((String) t)));
        conversors.register("string-to-boolean", ((t) -> Boolean.parseBoolean((String) t)));
        conversors.register("name-to-player", ((t) -> Bukkit.getPlayer((String) t)));
        conversors.register("uuid-to-player", ((t) -> Bukkit.getPlayer((UUID) t)));
        conversors.register("string-to-string", ((t) -> (t)));

        init();

        this.serviceManager.getService(ICommandService.class).get().register(conversors);
    }

    @Override
    public void onDisable() {
        shutdown();
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    public Registry<String, TypeConversor<?, ?>> getConversors() {
        return conversors;
    }

    public void init() {

    }

    public void shutdown() {

    }

}
