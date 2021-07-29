package net.threader.lib.text;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.threader.lib.text.color.HexColor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class Text implements IText {
    private List<Object> objects;

    public Text(List<Object> objects) {
        this.objects = objects;
    }

    @Override
    public List<Object> getObjects() {
        return objects;
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
        private List<Object> objects;

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