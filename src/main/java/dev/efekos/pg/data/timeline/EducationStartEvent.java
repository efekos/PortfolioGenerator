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

package dev.efekos.pg.data.timeline;

import dev.efekos.pg.data.schema.Date;
import dev.efekos.pg.data.schema.MonthDate;
import dev.efekos.pg.data.type.EducationEntryType;
import dev.efekos.pg.util.Text;

public class EducationStartEvent implements TimelineEvent {
    private final String name;
    private final MonthDate when;
    private final EducationEntryType entryType;

    public EducationStartEvent(String name, MonthDate when, EducationEntryType entryType) {
        this.name = name;
        this.when = when;
        this.entryType = entryType;
    }

    @Override
    public String getIcon() {
        return switch (entryType) {
            case UNIVERSITY -> "university";
            case SCHOOL -> "school";
            case COURSE -> "book";
            case DEGREE -> "degree";
        };
    }

    @Override
    public String getTitle() {
        return switch (entryType) {
            case COURSE -> Text.translated("timeline.education.start.course", name);
            case DEGREE -> Text.translated("timeline.education.start.degree", name);
            case SCHOOL, UNIVERSITY -> Text.translated("timeline.education.start.school", name);
        };
    }

    @Override
    public String getTime() {
        return when.toString();
    }

    @Override
    public Date getDate() {
        return when;
    }
}
