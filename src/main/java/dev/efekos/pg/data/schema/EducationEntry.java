package dev.efekos.pg.data.schema;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.RequiredDataType;

public class EducationEntry implements JsonSchema{
    private EducationEntryIcon icon;
    private String title;
    private MonthDate start;
    private MonthDate until;
    private String location;

    public EducationEntry(EducationEntryIcon icon, String title, MonthDate start, MonthDate until, String location) {
        this.icon = icon;
        this.title = title;
        this.start = start;
        this.until = until;
        this.location = location;
    }

    @Override
    public void readJson(JsonObject object, DataGrabberContext context) throws JsonParseException {
        DataTypeChecker checker = new DataTypeChecker(context.getCurrentFile());

        checker.searchExceptions(object,"title", RequiredDataType.STRING);
        checker.searchExceptions(object,"");
    }

    public EducationEntryIcon getIcon() {
        return icon;
    }

    public void setIcon(EducationEntryIcon icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MonthDate getStart() {
        return start;
    }

    public void setStart(MonthDate start) {
        this.start = start;
    }

    public MonthDate getUntil() {
        return until;
    }

    public void setUntil(MonthDate until) {
        this.until = until;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
