package dev.efekos.pg.data.type;

public enum ProjectLinkType {
    ISSUES("issues","Issue Tracker"),
    SOURCE("source","Source"),
    SUPPORT("support","Support"),
    DONATE("donate","Donate"),
    WIKI("wiki","Wiki"),
    DOCUMENTATION("doc","Documentation"),
    GUIDE("guide","Guide"),
    WEBSITE("main","Official Website")
    ;
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
}