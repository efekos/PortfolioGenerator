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
import dev.efekos.pg.data.timeline.JobEndEvent;
import dev.efekos.pg.data.timeline.JobStartEvent;
import dev.efekos.pg.data.timeline.TimelineEvent;
import dev.efekos.pg.data.timeline.TimelineEventSource;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.RequiredDataType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ExperienceEntry implements JsonSchema, TimelineEventSource {
            private String company;
    @Setter private String position;
    @Setter private MonthDate from;
    @Setter private MonthDate to;
            private boolean currentJob;

    @Override
    public List<TimelineEvent> getEvents() {
        ArrayList<TimelineEvent> list = new ArrayList<>();
        list.add(new JobStartEvent(company, position, from));
        if (!currentJob) list.add(new JobEndEvent(company, to));

        return list;
    }

    public ExperienceEntry() {

    }

    @Override
    public void readJson(JsonElement element, DataGrabberContext context) throws JsonParseException {
        DataTypeChecker checker = new DataTypeChecker(context.getCurrentFile());
        checker.expectObject(element);
        JsonObject object = element.getAsJsonObject();

        checker.searchExceptions(object, "company", RequiredDataType.STRING);
        checker.searchExceptions(object, "position", RequiredDataType.STRING);

        this.company = object.get("company").getAsString();
        this.position = object.get("position").getAsString();


        if (object.has("current_job")) {

            checker.searchExceptions(object, "current_job", RequiredDataType.BOOLEAN);

            this.currentJob = object.get("current_job").getAsBoolean();

        } else {
            this.currentJob = false;
        }

        MonthDate fromDate = new MonthDate(0, 0);
        fromDate.readJson(object.get("from"), context);

        MonthDate toDate = new MonthDate(0, 0);
        toDate.readJson(object.get("to"), context);

        this.from = fromDate;
        this.to = toDate;
    }
}