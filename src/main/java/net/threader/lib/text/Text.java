package net.threader.lib.text;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.threader.lib.text.color.HexColor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Text implements IText {
    private List<Object> objects;
    private Map<String, Object> variables;

    public Text(List<Object> objects, Map<String, Object> variables) {
        this.variables = variables;
        this.objects = this.translateVariables(objects);
    }

    @Override
    public List<Object> getObjects() {
        return objects;
    }

    private List<Object> translateVariables(List<Object> objects) {
        List<Object> translated = new ArrayList<>();
        objects.stream().forEach(object -> {
            if(object instanceof String) {
                String currentText = (String) object;
                if(currentText.contains("${")) {
                    char[] charSequence = currentText.toCharArray();
                    int firstIndex = currentText.indexOf("${") + 2;
                    if(currentText.substring(firstIndex).contains("}")) {
                        StringBuilder var = new StringBuilder();
                        for(int i = firstIndex; i < charSequence.length; i++) {
                            if(charSequence[i] == '}') {
                                if (variables.containsKey(var.toString())) {
                                    currentText = currentText.replace("${" + var + "}", variables.get(var.toString()).toString());
                                    break;
                                }
                            }
                            var.append(charSequence[i]);
                        }
                    }
                }
            } else {
                translated.add(object);
            }
        });
        return translated;
    }

    @Override
    public BaseComponent[] build() {
        ComponentBuilder builder = new ComponentBuilder();
        objects.forEach(obj -> {
            if(obj instanceof String) {
                builder.append((String) obj);
            }
            if(obj instanceof Style) {
                Style style = (Style) obj;
                try {
                    builder.getClass().getDeclaredMethod(style.getMethod()).invoke(builder, Boolean.TRUE);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
            if(obj instanceof ChatColor) {
                builder.color(((ChatColor) obj).asBungee());
            }
            if(obj instanceof HexColor) {
                builder.color(net.md_5.bungee.api.ChatColor.of(((HexColor) obj).getHex()));
            }
        });
        return builder.create();
    }

    @Override
    public void send(Player player) {
        player.spigot().sendMessage(this.build());
    }

    public static class Builder {
        private List<Object> objects = new ArrayList<>();
        private Map<String, Object> variables = new HashMap<>();

        public Builder color(String hex) {
            objects.add(new HexColor(hex));
            return this;
        }

        public Builder color(ChatColor color) {
            objects.add(color);
            return this;
        }

        public Builder message(String message) {
            objects.add(message);
            return this;
        }

        public Builder style(Style style) {
            objects.add(style);
            return this;
        }

        public Builder variable(String id, Object value) {
            variables.put(id, value);
            return this;
        }

        public Text build() {
            return new Text(objects, variables);
        }

    }
}
