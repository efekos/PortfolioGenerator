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
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.RequiredDataType;
import dev.efekos.pg.util.DateHelper;

import java.util.regex.Pattern;

public class MonthDate implements JsonSchema, Comparable<MonthDate> {
    private Integer month;
    private Integer year;

    public MonthDate(int month, int year) {
        this.month = month;
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public void readJson(JsonElement element, DataGrabberContext context) throws JsonParseException {
        DataTypeChecker checker = new DataTypeChecker(context.getCurrentFile());

        if (element.isJsonObject()) parseObject(checker, element.getAsJsonObject());
        else parseString(element.getAsString());
    }

    private void parseString(String date) {

        if (!STRING_DATE_PATTERN.matcher(date).matches()) throw new JsonParseException("Invalid date");

        String[] members = date.split("-");

        this.year = Integer.parseInt(members[0]);
        this.month = Integer.parseInt(members[1]);
    }

    private void parseObject(DataTypeChecker checker, JsonObject object) {
        checker.searchExceptions(object, "month", RequiredDataType.INTEGER);
        checker.searchExceptions(object, "year", RequiredDataType.INTEGER);

        this.month = object.get("month").getAsInt();
        this.year = object.get("year").getAsInt();
    }

    private final Pattern STRING_DATE_PATTERN = Pattern.compile("[0-9]{4}-[0-9]{2}");

    @Override
    public int compareTo(MonthDate other) {
        // Compare years first
        int yearComparison = this.year.compareTo(other.year);

        // If years are the same, compare months
        if (yearComparison == 0) {
            return this.month.compareTo(other.month);
        } else {
            return yearComparison;
        }
    }

    @Override
    public String toString() {
        return DateHelper.monthToString(month) + ", " + year;
    }
}