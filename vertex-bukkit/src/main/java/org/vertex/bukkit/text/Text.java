package org.vertex.bukkit.text;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Text {
    protected Map<String, Object> placeholders = new HashMap<>();
    protected List<String> lines = new ArrayList<>();

    public Text(Map<String, Object> placeholders, List<String> lines) {
        this.placeholders = placeholders;
        this.lines = lines;
        colorizeLines();
    }

    public Text() {
    }

    public List<String> getLines() {
        return lines;
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
        lines.forEach(line -> player.sendMessage(replaceVariables(line)));
    }

}
