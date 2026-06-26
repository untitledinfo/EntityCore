package firepdx.pgc.net.npc;

public record SkinData(String source, String value, String signature) {
    public static SkinData username(String username) {
        return new SkinData("username", username, "");
    }

    public static SkinData empty() {
        return new SkinData("", "", "");
    }
}
