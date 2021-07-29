package net.threader.lib;

import net.threader.lib.text.Style;
import org.bukkit.ChatColor;

public class Test {
    public static void main(String[] args) {
        String test = "hello &fworld &#123456h";
        char[] chars = test.toCharArray();
        for(int i = 0; i < chars.length; i++) {
            StringBuilder currentMessage = new StringBuilder();
            if(i != 0 && chars[i - 1] == '&') {
                continue;
            }
            if(chars[i] == '&' && !(i == chars.length - 1)) {
                System.out.print(currentMessage);
                currentMessage = new StringBuilder();
                char code = chars[i + 1];
                if(code == '#') {
                    System.out.println(chars.length - i);
                    StringBuilder hex = new StringBuilder();
                    if(chars.length - i + 1 >= 9) {
                        for(int j = 1; j<7; j++) {
                            hex.append(chars[i + j]);
                        }
                        i += 7;
                    }
                } else if (Style.byCode(code).isPresent()) {
                    //builder.style(Style.byCode(code).get());
                } /*else if (ChatColor.getByChar(code) != null) {
                    builder.color(ChatColor.getByChar(code));
                }*/ else {
                    currentMessage.append(chars[i]);
                }
            } else {
                currentMessage.append(chars[i]);
            }
            System.out.print(currentMessage);
        }
    }
}
