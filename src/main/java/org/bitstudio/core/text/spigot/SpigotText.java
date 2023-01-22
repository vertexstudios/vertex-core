package org.bitstudio.core.text.spigot;

import net.md_5.bungee.api.ChatColor;
import org.bitstudio.core.text.Text;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SpigotText extends Text {
    private static final Pattern HEX_PATTERN = Pattern.compile("&(#\\w{6})");

    public SpigotText(Map<String, Object> placeholders, List<String> lines) {
        super(placeholders, lines);
    }

    public SpigotText() {
    }

    public void colorizeLines() {
        lines = lines.stream().map(line -> {
            Matcher matcher = HEX_PATTERN.matcher(ChatColor.translateAlternateColorCodes('&', line));
            StringBuffer buffer = new StringBuffer();

            while (matcher.find()) {
                matcher.appendReplacement(buffer, ChatColor.of(matcher.group(1)).toString());
            }

            return matcher.appendTail(buffer).toString();
        }).collect(Collectors.toList());
    }
}
