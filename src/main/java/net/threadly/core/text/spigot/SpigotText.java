package net.threadly.core.text.spigot;

import net.md_5.bungee.api.ChatColor;
import net.threadly.core.text.Text;

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
        System.out.println("Chamou o spigotcolor");
        lines = lines.stream().map(line -> {
            Matcher matcher = HEX_PATTERN.matcher(ChatColor.translateAlternateColorCodes('&', line));
            StringBuffer buffer = new StringBuffer();

            while (matcher.find()) {
                System.out.println("Cor: " + matcher.group());
                System.out.println("Cor: " + matcher.group(1));
                matcher.appendReplacement(buffer, ChatColor.of(matcher.group(1)).toString());
            }

            return matcher.appendTail(buffer).toString();
        }).collect(Collectors.toList());
    }
}
