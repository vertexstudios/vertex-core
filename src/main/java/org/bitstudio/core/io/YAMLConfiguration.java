package org.bitstudio.core.io;

import org.bitstudio.core.PluginContainer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class YAMLConfiguration {

    private File file;
    private FileConfiguration config;

    public YAMLConfiguration(String fileName) throws InvalidConfigurationException, IOException {

        String dir = fileName.endsWith(".yml") ? fileName : fileName + ".yml";
        File configFile = new File(PluginContainer.getCurrentPlugin().getDataFolder(), dir);

        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            PluginContainer.getCurrentPlugin().saveResource(dir, false);
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
