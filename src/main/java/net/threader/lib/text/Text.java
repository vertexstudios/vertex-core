package net.threader.lib.text;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.threader.lib.text.color.HexColor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Text implements IText {
    private List<Object> objects;
    private Map<String, Object> variables = new HashMap<>();

    public Text(List<Object> objects) {
        this.objects = this.translateVariables(objects);
    }

    public Text addVariable(String id, Object value) {
        variables.put(id, value);
        return this;
    }

    public static Text of(String text) {
        char[] chars = text.toCharArray();
        Builder builder = builder();
        for(int i = 0; i < chars.length; i++) {
            StringBuilder currentMessage = new StringBuilder();
            if(i != 0 && chars[i - 1] == '&') {
                continue;
            }
            if(chars[i] == '&' && !(i == chars.length - 1)) {
                builder.message(currentMessage.toString());
                currentMessage = new StringBuilder();
                char code = chars[i + 1];
                if(code == '#') {
                    StringBuilder hex = new StringBuilder();
                    if(chars.length - (i + 1) >= 8) {
                        for(int j = 1; j<7; j++) {
                            hex.append(chars[i + j]);
                        }
                        builder.color(hex.toString());
                        i += 7;
                    }
                } else if (Style.byCode(code).isPresent()) {
                    builder.style(Style.byCode(code).get());
                } else if (ChatColor.getByChar(code) != null) {
                    builder.color(ChatColor.getByChar(code));
                } else {
                    currentMessage.append(chars[i]);
                }
            } else {
                currentMessage.append(chars[i]);
            }
        }
        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public List<Object> getObjects() {
        return objects;
    }

    private List<Object> translateVariables(List<Object> objects) {
        List<Object> translated = new ArrayList<>();
        objects.forEach(object -> {
            if(object instanceof String) {
                AtomicReference<String> currentText = new AtomicReference<>((String) object);
                variables.forEach((id, value) -> currentText.set(currentText.get().replace("${" + id + "}", value.toString())));
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

        public Text build() {
            return new Text(objects);
        }

    }
}
