package net.threader.lib.text;

public enum Style {
    ITALIC("italic"),
    UNDERLINE("underlined"),
    STRIPE("strikethrough"),
    BOLD("bold"),
    OBFUSCATED("obfuscated"),
    RESET("reset");

    private String method;

    Style(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

}
