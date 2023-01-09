package net.threadly.core.io;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class YAMLConfiguration {

    private File file;
    private FileConfiguration config;

    public YAMLConfiguration(Plugin plugin, String fileName) throws InvalidConfigurationException, IOException {

        String dir = fileName.endsWith(".yml") ? fileName : fileName + ".yml";
        File configFile = new File(plugin.getDataFolder(), dir);

        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource(dir, false);
        }

        this.file = configFile;
        this.config = YamlConfiguration.loadConfiguration(file);
        reload();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void reload() throws InvalidConfigurationException, IOException {
        config.load(file);
    }

    public void save() throws IOException {
        config.save(file);
    }
}
