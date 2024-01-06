package dev.efekos.pg.data.schema;

import com.google.gson.*;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.RequiredDataType;

import java.util.ArrayList;
import java.util.List;

public class EducationInfo implements JsonSchema{
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
    public void readJson(JsonObject object, DataGrabberContext context) throws JsonParseException {
        DataTypeChecker checker = new DataTypeChecker(context.getCurrentFile());

        checker.searchExceptions(object,"entries", RequiredDataType.ARRAY);

        JsonArray array = object.get("entries").getAsJsonArray();
        List<EducationEntry> entryList = new ArrayList<>();
        for (JsonElement element : array) {
            if(!element.isJsonObject()) throw new JsonSyntaxException("Elements in 'entries' must be an object at '"+context.getCurrentFile()+"'");
            EducationEntry entry = new EducationEntry(EducationEntryIcon.SCHOOL, null, null, null, null);
            entry.readJson(element.getAsJsonObject(),context);
            entryList.add(entry);
        }
        this.entries = entryList;
    }
}
