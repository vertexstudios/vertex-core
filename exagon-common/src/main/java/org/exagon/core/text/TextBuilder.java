package org.exagon.core.text;

import org.exagon.core.PluginContainer;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class TextBuilder {

    List<String> lines = new ArrayList<>();
    private Map<String, Object> placeholders = new HashMap<>();

    public TextBuilder addLine(String line) {
        lines.add(line);
        return this;
    }

    public TextBuilder addLines(String... lines) {
        this.lines.addAll(Arrays.asList(lines));
        return this;
    }

    public TextBuilder addLines(List<String> lines) {
        this.lines.addAll(lines);
        return this;
    }

    public TextBuilder fromConfig(String path) {
        FileConfiguration config = PluginContainer.getCurrentPlugin().getConfig();
        path = "message." + path;
        if(config.get(path) instanceof List) {
            addLines(config.getStringList(path));
        } else {
            addLine(config.getString(path));
        }
        return this;
    }

    public TextBuilder fromConfig(String path, FileConfiguration config) {
        if(config.get(path) instanceof List) {
            addLines(config.getStringList(path));
        } else {
            addLine(config.getString(path));
        }
        return this;
    }

    public TextBuilder setPlaceholder(String var, Object value) {
        placeholders.put(var, value);
        return this;
    }

    public <T extends Text> T build(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor(Map.class, List.class).newInstance(placeholders, lines);
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}
