package dev.efekos.pg.data.schema;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.RequiredDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GeneralInfo implements JsonSchema{
    private String name;
    private DayDate birthDate;
    private String title;
    private Locale nativeLanguage;
    private List<Locale> knownLanguages;

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
        DataTypeChecker checker = new DataTypeChecker();
        checker.setCurrentFile(context.getCurrentFile());

        checker.searchExceptions(object,"name", RequiredDataType.STRING);
        checker.searchExceptions(object,"title", RequiredDataType.STRING);
        checker.searchExceptions(object,"birth", RequiredDataType.OBJECT);
        checker.searchExceptions(object,"native_language", RequiredDataType.STRING);

        // name, title
        this.name = object.get("name").getAsString();
        this.title = object.get("title").getAsString();

        // birth
        this.birthDate = new DayDate(26,2,2010);
        birthDate.readJson(object.get("birth").getAsJsonObject(),context);

        // native_language
        this.nativeLanguage = Locale.forLanguageTag(object.get("native_language").getAsString());

        // known_languages
        try {

            checker.searchExceptions(object,"known_languages", RequiredDataType.ARRAY);
            this.knownLanguages = object
                    .get("known_languages")
                    .getAsJsonArray().asList()
                    .stream().map(jsonElement -> Locale.forLanguageTag(jsonElement.getAsString()))
                    .toList();

        } catch (Exception e){
            this.knownLanguages = new ArrayList<>();
        }

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
