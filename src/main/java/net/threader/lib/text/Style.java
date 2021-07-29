package net.threader.lib.text;

public enum Style {
    ITALIC("o"),
    UNDERLINE("n"),
    STRIPE("m"),
    BOLD("l"),
    OBFUSCATED("k"),
    RESET("r");

    private String code;

    Style(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
