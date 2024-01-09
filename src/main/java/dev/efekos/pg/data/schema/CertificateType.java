package dev.efekos.pg.data.schema;

public enum CertificateType {
    COURSE("course"),
    SCHOOL("school"),
    UNIVERSITY("university"),
    PROJECT("project"),
    DEGREE("degree");

    private final String id;

    CertificateType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
