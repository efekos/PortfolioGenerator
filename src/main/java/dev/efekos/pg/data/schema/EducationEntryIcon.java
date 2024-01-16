package dev.efekos.pg.data.schema;

public enum EducationEntryIcon {
    SCHOOL("school"),
    UNIVERSITY("university"),
    DEGREE("degree"),
    COURSE("course");

    private final String id;

    EducationEntryIcon(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
