/*
 * Copyright 2024 efekos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the “Software”), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package dev.efekos.pg.data.type;

import com.google.gson.JsonParseException;

import java.util.Arrays;
import java.util.Optional;

public enum SocialLinkType {
    CURSEFORGE("curseforge", "rgb(59, 41, 29)", "rgb(73, 50, 35)"),
    DISCORD("discord", "rgb(49, 50, 122)", "rgb(63, 69, 158)"),
    FACEBOOK("facebook", "rgb(20, 62, 83)", "rgb(32, 91, 126)"),
    GITHUB("github", "rgb(45, 46, 48)", "rgb(54, 55, 58)"),
    INSTAGRAM("instagram", "rgb(70, 20, 83)", "rgb(99, 32, 126)"),
    MODRINTH("modrinth", "rgb(16, 128, 12)", "rgb(32, 148, 32)"),
    REDDIT("reddit", "rgb(128, 74, 12)", "rgb(148, 88, 32)"),
    SPOTIFY("spotify", "rgb(20, 83, 30)", "rgb(32, 126, 55)"),
    STEAM("steam", "rgb(48, 56, 68)", "rgb(73, 81, 97)"),
    TIKTOK("tiktok", "rgb(199, 195, 199)", "rgb(250, 250, 250)"),
    TWITCH("twitch", "rgb(72, 40, 105)", "rgb(98, 52, 145)"),
    X("x", "rgb(68, 68, 68)", "rgb(94, 94, 94)"),
    TWITTER("twitter", "rgb(12, 91, 128)", "rgb(32, 121, 148)"),
    THREADS("threads", "rgb(58, 53, 59)", "rgb(80, 73, 82)"),
    NPM("npm", "rgb(145, 93, 93)", "rgb(173, 101, 101)"),
    STACKOVERFLOW("stackoverflow", "rgb(156, 110, 58)", "rgb(184, 130, 70)"),
    CROWDIN("crowdin", "rgb(32, 38, 41)", "rgb(48, 59, 64)"),
    VSCODE("vscode_marketplace", "rgb(20, 68, 107)", "rgb(29, 93, 145)"),
    PATREON("patreon", "rgb(89, 74, 67)", "rgb(125, 103, 92)"),
    BUY_ME_A_COFFEE("buy_me_a_coffee", "rgb(110, 103, 26)", "rgb(133, 124, 32)"),
    KOFI("kofi", "rgb(110, 41, 49)", "rgb(135, 53, 62)"),
    LINKEDIN("linkedin", "rgb(35, 79, 110)", "rgb(52, 114, 158)"),
    FIVERR("fiverr", "rgb(31, 126, 49)", "rgb(53, 172, 76)"),
    BIONLUK("bionluk", "#5e222d", "#803341"),
    SNAPCHAT("snapchat", "#706f34", "#96953f"),
    KICK("kick", "#287d15", "#359c1e"),
    QUORA("quora", "#6b2e2e", "#a35656"),
    PINTEREST("pinterest", "rgb(153, 54, 54)", "#bf4d4d"),
    TUMBLR("tumblr", "#252f4a", "#3d4b73"),
    YOUTUBE("youtube", "rgb(128, 12, 12)", "rgb(148, 32, 32)");

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

    public static SocialLinkType findById(String id) {
        Optional<SocialLinkType> type = Arrays.stream(values()).filter(socialLinkType -> socialLinkType.id.equals(id)).findFirst();
        if (type.isEmpty()) throw new JsonParseException("Unknown social link type '" + id + "'");
        return type.get();
    }

    public static boolean isValidId(String id) {
        return Arrays.stream(values()).anyMatch(socialLinkType -> socialLinkType.id.equals(id));
    }
}
