package net.threadly.core.text.color;

public class HexColor {

    private String hex;

    public HexColor(String hex) {
        this.hex = hex;
    }

    public static HexColor of(String hex) {
        return new HexColor(hex);
    }

    public String getHex() {
        return hex;
    }
}
