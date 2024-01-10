package dev.efekos.pg.data.schema;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.RequiredDataType;

import java.util.Arrays;
import java.util.Optional;

public class EducationEntry implements JsonSchema {
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

        checker.searchExceptions(object, "title", RequiredDataType.STRING);
        checker.searchExceptions(object, "icon", RequiredDataType.STRING);
        checker.searchExceptions(object, "location", RequiredDataType.STRING);
        checker.searchExceptions(object, "start", RequiredDataType.OBJECT);
        checker.searchExceptions(object, "until", RequiredDataType.OBJECT);

        // title,location
        this.title = object.get("title").getAsString();
        this.location = object.get("location").getAsString();

        // icon
        String iconString = object.get("icon").getAsString();
        Optional<EducationEntryIcon> entryIcon = Arrays.stream(EducationEntryIcon.values()).filter(educationEntryIcon -> educationEntryIcon.getId().equals(iconString)).findFirst();
        if (!entryIcon.isPresent())
            throw new JsonParseException("Unknown education entry icon type '" + iconString + "' in file '" + context.getCurrentFile() + "'");
        this.icon = entryIcon.get();

        // start,until

        MonthDate startDate = new MonthDate(0, 0);
        startDate.readJson(object.get("start").getAsJsonObject(), context);

        MonthDate untilDate = new MonthDate(0, 0);
        startDate.readJson(object.get("until").getAsJsonObject(), context);

        this.start = startDate;
        this.until = untilDate;
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
