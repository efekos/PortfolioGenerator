package dev.efekos.pg.data.schema;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.efekos.pg.data.DataGrabberContext;

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
    public void readJson(JsonObject object, DataGrabberContext context) {
        if(!object.has("name")) throw new JsonSyntaxException("'name' missing in file 'general.json'");
        if(!object.has("title")) throw new JsonSyntaxException("'title' missing in file 'general.json'");
        if(!object.has("birth")) throw new JsonSyntaxException("'birth' missing in file 'general.json'");

        this.name = object.get("name").getAsString();
        this.title = object.get("title").getAsString();
        this.birthDate = new DayDate(26,2,2010);
        JsonElement element = object.get("birth");
        if(!element.isJsonObject()) throw new JsonSyntaxException("'birth' must be an object.");
        birthDate.readJson(element.getAsJsonObject(),context);
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
