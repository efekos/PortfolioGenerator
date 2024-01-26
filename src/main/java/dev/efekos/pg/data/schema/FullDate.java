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

public class FullDate implements JsonSchema, Comparable<FullDate> {
    private Integer day;
    private Integer month;
    private Integer year;
    private Integer minute;
    private Integer hour;
    private Integer second;

    public FullDate(int day, int month, int year, int minute, int hour, int second) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.minute = minute;
        this.hour = hour;
        this.second = second;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
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

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    @Override
    public void readJson(JsonElement element, DataGrabberContext context) {
        DataTypeChecker checker = new DataTypeChecker(context.getCurrentFile());

        if (element.isJsonObject()) parseObject(checker, element.getAsJsonObject());
        else parseString(element.getAsString());
    }

    private void parseString(String date) {

        if (!STRING_DATE_PATTERN.matcher(date).matches()) throw new JsonParseException("Invalid date");

        String[] members = date.split("-");

        this.year = Integer.parseInt(members[0]);
        this.month = Integer.parseInt(members[1]);
        this.day = Integer.parseInt(members[2]);
        this.hour = Integer.parseInt(members[3]);
        this.minute = Integer.parseInt(members[4]);
        this.second = Integer.parseInt(members[5]);
    }

    private final Pattern STRING_DATE_PATTERN = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}-[0-9]{2}-[0-9]{2}");

    private void parseObject(DataTypeChecker checker, JsonObject object) {
        checker.searchExceptions(object, "day", RequiredDataType.INTEGER);
        checker.searchExceptions(object, "month", RequiredDataType.INTEGER);
        checker.searchExceptions(object, "year", RequiredDataType.INTEGER);
        checker.searchExceptions(object, "minute", RequiredDataType.INTEGER);
        checker.searchExceptions(object, "hour", RequiredDataType.INTEGER);
        checker.searchExceptions(object, "second", RequiredDataType.INTEGER);

        this.day = object.get("day").getAsInt();
        this.month = object.get("month").getAsInt();
        this.year = object.get("year").getAsInt();
        this.minute = object.get("minute").getAsInt();
        this.hour = object.get("hour").getAsInt();
        this.second = object.get("second").getAsInt();
    }

    @Override
    public int compareTo(FullDate other) {
        int yearc = year.compareTo(other.year);
        int monthc = month.compareTo(other.month);
        int dayc = day.compareTo(other.day);
        int hourc = hour.compareTo(other.hour);
        int minc = minute.compareTo(other.minute);
        int secc = second.compareTo(other.second);

        if (yearc == 0) {
            if (monthc == 0) {
                if (dayc == 0) {
                    if (hourc == 0) {
                        if (minc == 0) {
                            return secc;
                        } else return minc;
                    } else return hourc;
                } else return dayc;
            } else return monthc;
        } else return yearc;
    }

    @Override
    public String toString() {
        return hour + ":" + minute + ":" + second + ", " + DateHelper.monthToString(month) + " " + day+DateHelper.getThing(day) + ", " + year;
    }
}
