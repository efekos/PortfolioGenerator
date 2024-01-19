package dev.efekos.pg.data.schema;

import java.util.Arrays;

public enum SocialLinkType {
    CURSEFORGE("curseforge","rgb(59, 41, 29)","rgb(73, 50, 35)"),
    DISCORD("discord","rgb(49, 50, 122)","rgb(63, 69, 158)"),
    FACEBOOK("facebook","rgb(20, 62, 83)","rgb(32, 91, 126)"),
    GITHUB("github","rgb(45, 46, 48)","rgb(54, 55, 58)"),
    INSTAGRAM("instagram","rgb(70, 20, 83)","rgb(99, 32, 126)"),
    MODRINTH("modrinth","rgb(16, 128, 12)","rgb(32, 148, 32)"),
    REDDIT("reddit","rgb(128, 74, 12)","rgb(148, 88, 32)"),
    SPOTIFY("spotify","rgb(20, 83, 30)","rgb(32, 126, 55)"),
    STEAM("steam","rgb(48, 56, 68)","rgb(73, 81, 97)"),
    TIKTOK("tiktok","rgb(199, 195, 199)","rgb(250, 250, 250)"),
    TWITCH("twitch","rgb(72, 40, 105)","rgb(98, 52, 145)"),
    X("x","rgb(68, 68, 68)","rgb(94, 94, 94)"),
    TWITTER("twitter","rgb(12, 91, 128)","rgb(32, 121, 148)"),
    YOUTUBE("youtube","rgb(128, 12, 12)","rgb(148, 32, 32)");

    private final String id;
    private final String normalColor;
    private final String highlightColor;

    SocialLinkType(String id, String normalColor, String highlightColor) {
        this.id = id;
        this.normalColor = normalColor;
        this.highlightColor = highlightColor;
    }

    public String getNormalColor() {
        return normalColor;
    }

    public String getHighlightColor() {
        return highlightColor;
    }

    public String getId() {
        return id;
    }

    public static SocialLinkType findById(String id){
        return Arrays.stream(values()).filter(socialLinkType -> socialLinkType.id.equals(id)).findFirst().get();
    }

    public static boolean isValidId(String id){
        return Arrays.stream(values()).anyMatch(socialLinkType -> socialLinkType.id.equals(id));
    }
}
