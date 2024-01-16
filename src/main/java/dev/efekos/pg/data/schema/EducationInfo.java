package dev.efekos.pg.data.schema;

import com.google.gson.*;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.RequiredDataType;

import java.util.ArrayList;
import java.util.List;

public class EducationInfo implements JsonSchema {
    private List<EducationEntry> entries;

    public EducationInfo(List<EducationEntry> entries) {
        this.entries = entries;
    }

    public EducationInfo() {
    }

    public List<EducationEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<EducationEntry> entries) {
        this.entries = entries;
    }

    @Override
    public void readJson(JsonElement element, DataGrabberContext context) throws JsonParseException {
        DataTypeChecker checker = new DataTypeChecker(context.getCurrentFile());
        checker.expectObject(element);
        JsonObject object = element.getAsJsonObject();

        checker.searchExceptions(object, "entries", RequiredDataType.ARRAY);

        JsonArray array = object.get("entries").getAsJsonArray();
        List<EducationEntry> entryList = new ArrayList<>();
        for (JsonElement element1 : array) {
            if (!element1.isJsonObject())
                throw new JsonSyntaxException("Elements in 'entries' must be an object at '" + context.getCurrentFile() + "'");
            EducationEntry entry = new EducationEntry(EducationEntryIcon.SCHOOL, null, null, null, null);
            entry.readJson(element1.getAsJsonObject(), context);
            entryList.add(entry);
        }
        this.entries = entryList;
    }
}
