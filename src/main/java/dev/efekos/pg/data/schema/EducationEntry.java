/*
 * Copyright 2024 efekos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the “Software”), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package dev.efekos.pg.data.schema;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.timeline.EducationEndEvent;
import dev.efekos.pg.data.timeline.EducationStartEvent;
import dev.efekos.pg.data.timeline.TimelineEvent;
import dev.efekos.pg.data.timeline.TimelineEventSource;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.EducationEntryType;
import dev.efekos.pg.data.type.RequiredDataType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class EducationEntry implements JsonSchema, TimelineEventSource {
    private EducationEntryType type;
    private String title;
    private MonthDate start;
    private MonthDate until;
    private String location;

    @Override
    public List<TimelineEvent> getEvents() {
        return Arrays.asList(
                new EducationStartEvent(title,start),
                new EducationEndEvent(until,title,type)
        );
    }

    public EducationEntry(EducationEntryType type, String title, MonthDate start, MonthDate until, String location) {
        this.type = type;
        this.title = title;
        this.start = start;
        this.until = until;
        this.location = location;
    }

    @Override
    public void readJson(JsonElement element, DataGrabberContext context) throws JsonParseException {
        DataTypeChecker checker = new DataTypeChecker(context.getCurrentFile());
        checker.expectObject(element);
        JsonObject object = element.getAsJsonObject();

        checker.searchExceptions(object, "title", RequiredDataType.STRING);
        checker.searchExceptions(object, "type", RequiredDataType.STRING);
        checker.searchExceptions(object, "location", RequiredDataType.STRING);

        // title,location
        this.title = object.get("title").getAsString();
        this.location = object.get("location").getAsString();

        // type
        String iconString = object.get("type").getAsString();
        Optional<EducationEntryType> entryIcon = Arrays.stream(EducationEntryType.values()).filter(educationEntryType -> educationEntryType.getId().equals(iconString)).findFirst();
        if (entryIcon.isEmpty())
            throw new JsonParseException("Unknown education entry type type '" + iconString + "' in file '" + context.getCurrentFile() + "'");
        this.type = entryIcon.get();

        // start,until

        MonthDate startDate = new MonthDate(0, 0);
        startDate.readJson(object.get("start"), context);

        MonthDate untilDate = new MonthDate(0, 0);
        untilDate.readJson(object.get("until"), context);

        this.start = startDate;
        this.until = untilDate;
    }

    public EducationEntryType getType() {
        return type;
    }

    public void setType(EducationEntryType type) {
        this.type = type;
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
