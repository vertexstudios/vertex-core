package net.threadly.core.service;

import net.threadly.core.command.CommandSpec;
import net.threadly.core.conversor.TypeConversor;
import net.threadly.core.util.Registry;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;
import java.util.Set;

public interface ICommandService extends IService {
    void register(JavaPlugin plugin, Registry<String, TypeConversor<?,?>> conversors);
    void load(Class<?> clazz);
}
