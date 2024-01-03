package dev.efekos.pg.data;

public class GeneralInfo {
    private String name;
    private DayDate birthDate;
    private String title;

    public GeneralInfo(String name, DayDate birthDate, String title) {
        this.name = name;
        this.birthDate = birthDate;
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DayDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(DayDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
