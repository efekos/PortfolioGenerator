package dev.efekos.pg.data.type;

public enum VersionType {
    RELEASE("release","Release","rgb(99, 224, 118)"),
    BETA("beta","Beta","rgb(255, 111, 0)"),
    ALPHA("alpha","Alpha","rgb(255, 59, 20)"),
    PROTOTYPE("prototype","Prototype","rgb(64, 119, 230)"),
    SNAPSHOT("snapshot","Snapshot","rgb(245, 240, 93)"),
    RELEASE_CANDIDATE("rc","Release Candidate","rgb(130, 219, 94)"),
    PRE_RELEASE("pre_release","Pre-Release","rgb(110, 219, 66)")
    ;

    private final String id;
    private final String display;
    private final String color;

    VersionType(String id, String display, String color) {
        this.id = id;
        this.display = display;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    public String getColor() {
        return color;
    }
}
