package net.threadly.core.text;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Text {
    protected Map<String, Object> placeholders = new HashMap<>();
    protected List<String> lines = new ArrayList<>();

    public Text(Map<String, Object> placeholders, List<String> lines) {
        this.placeholders = placeholders;
        this.lines = lines;
    }

    public Text() {
    }

    public void colorizeLines() {
        lines = lines.stream().map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList());
    }

    public String replaceVariables(String line) {
        AtomicReference<String> currentLine = new AtomicReference<>(line);
        placeholders.forEach((key, value) -> {
            if (line.contains("{" + key + "}")) {
                currentLine.set(currentLine.get().replace("{" + key + "}", value.toString()));
            }
        });
        return currentLine.get();
    }

    public void send(Player player) {
        colorizeLines();
        lines.forEach(line -> player.sendMessage(replaceVariables(line)));
    }

}
