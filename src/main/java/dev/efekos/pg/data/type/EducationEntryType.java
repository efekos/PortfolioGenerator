package dev.efekos.pg.data.type;

public enum EducationEntryType {
    SCHOOL("school","School"),
    UNIVERSITY("university","University"),
    DEGREE("degree","Degree"),
    COURSE("course","Course");

    private final String id;
    private final String display;

    EducationEntryType(String id, String display) {
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
