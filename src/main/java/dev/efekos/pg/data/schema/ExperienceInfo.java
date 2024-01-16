package dev.efekos.pg.data.schema;

import com.google.gson.*;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.RequiredDataType;

import java.util.ArrayList;
import java.util.List;

public class ExperienceInfo implements JsonSchema {

    private List<ExperienceEntry> entries;

    public ExperienceInfo(List<ExperienceEntry> entries) {
        this.entries = entries;
    }

    public List<ExperienceEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<ExperienceEntry> entries) {
        this.entries = entries;
    }

    @Override
    public void readJson(JsonElement element, DataGrabberContext context) throws JsonParseException {
        DataTypeChecker checker = new DataTypeChecker(context.getCurrentFile());
        checker.expectObject(element);
        JsonObject object = element.getAsJsonObject();

        checker.searchExceptions(object, "entries", RequiredDataType.ARRAY);

        JsonArray array = object.get("entries").getAsJsonArray();
        ArrayList<ExperienceEntry> experienceList = new ArrayList<>();

        for (JsonElement element1 : array) {
            if (!element1.isJsonObject()) throw new JsonSyntaxException("elements in 'entries' must be an object");

            ExperienceEntry entry = new ExperienceEntry();
            entry.readJson(element1.getAsJsonObject(), context);
            experienceList.add(entry);
        }

        this.entries = experienceList;
    }
}
