package net.threader.lib.text;

import java.util.Arrays;
import java.util.Optional;

public enum Style {
    ITALIC("italic", 'o'),
    UNDERLINE("underlined", 'n'),
    STRIPE("strikethrough", 'm'),
    BOLD("bold", 'l'),
    OBFUSCATED("obfuscated", 'k'),
    RESET("reset", 'r');

    private char code;
    private String method;

    Style(String method, char code) {
        this.method = method;
        this.code = code;
    }

    public String getMethod() {
        return method;
    }

    public char getCode() {
        return code;
    }

    public static Optional<Style> byCode(char code) {
        return Arrays.stream(values()).filter(x -> x.getCode() == code).findFirst();
    }
}
