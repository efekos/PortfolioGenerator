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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.timeline.TimelineEvent;
import dev.efekos.pg.data.timeline.TimelineEventSource;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.EducationEntryType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EducationInfo implements JsonSchema, TimelineEventSource {

    private List<EducationEntry> entries;

    public EducationInfo() {
    }

    @Override
    public List<TimelineEvent> getEvents() {
        List<TimelineEvent> events = new ArrayList<>();

        for (EducationEntry entry : entries) {
            events.addAll(entry.getEvents());
        }

        return events;
    }

    @Override
    public void readJson(JsonElement element, DataGrabberContext context) throws JsonParseException {
        DataTypeChecker checker = new DataTypeChecker(context.getCurrentFile());
        checker.expectArray(element);

        JsonArray array = element.getAsJsonArray();
        List<EducationEntry> entryList = new ArrayList<>();
        for (JsonElement element1 : array) {
            if (!element1.isJsonObject())
                throw new JsonSyntaxException("Elements in 'education.json' must be an object");
            EducationEntry entry = new EducationEntry(EducationEntryType.SCHOOL, null, null, null, null);
            entry.readJson(element1.getAsJsonObject(), context);
            entryList.add(entry);
        }
        this.entries = entryList;
    }
}
