package dev.efekos.pg.data.type;

import java.util.Arrays;

public enum ProjectLinkType {
    ISSUES("issues", "Issue Tracker"),
    SOURCE("source", "Source"),
    SUPPORT("support", "Support"),
    DONATE("donate", "Donate"),
    WIKI("wiki", "Wiki"),
    DOCUMENTATION("doc", "Documentation"),
    GUIDE("guide", "Guide"),
    WEBSITE("main", "Official Website");
    private final String id;
    private final String display;

    ProjectLinkType(String id, String display) {
        this.id = id;
        this.display = display;
    }

    public String getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    public static ProjectLinkType findById(String id) {
        return Arrays.stream(values()).filter(socialLinkType -> socialLinkType.id.equals(id)).findFirst().get();
    }

    public static boolean isValidId(String id) {
        return Arrays.stream(values()).anyMatch(socialLinkType -> socialLinkType.id.equals(id));
    }
}