package dev.efekos.pg.data.schema;

import com.google.gson.JsonObject;

public class GeneralInfo implements JsonSchema{
    private String name;
    private DayDate birthDate;
    private String title;

    public GeneralInfo(String name, DayDate birthDate, String title) {
        this.name = name;
        this.birthDate = birthDate;
        this.title = title;
    }

    public GeneralInfo() {
    }

    public String getName() {
        return name;
    }

    @Override
    public void readJson(JsonObject object) {

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
