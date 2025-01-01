/*
 * Copyright 2025 efekos
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
import dev.efekos.pg.util.Text;

import java.util.regex.Pattern;

/**
 * Represents a date that just needs a day, year, and month.
 */
public class DayDate implements JsonSchema, Date {
    private final Pattern STRING_DATE_PATTERN = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
    /**
     * Day of the date. Will be day of the month (such as 2nd July, but not Tuesday)
     */
    private Integer day;
    /**
     * Month of the date.
     */
    private Integer month;
    /**
     * Year of the date.
     */
    private Integer year;

    /**
     * Constructs a new {@link DayDate}
     *
     * @param day   Day of this date. Will be considered as day of the month (such as 2nd July, but not Tuesday).
     * @param month Month of this date.
     * @param year  Year of this date.
     */
    public DayDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    /**
     * Returns the day of this date
     *
     * @return Day value of this date
     */
    public int getDay() {
        return day;
    }

    /**
     * Changes the day of this date
     *
     * @param day New day value
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * Returns month value of this date.
     *
     * @return Month of this date.
     */
    public int getMonth() {
        return month;
    }

    /**
     * Changes the month of this date.
     *
     * @param month Month of this date.
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * Returns the year of this date.
     *
     * @return Year value of this date.
     */
    public int getYear() {
        return year;
    }

    /**
     * Changes the year value of this date.
     *
     * @param year New year value.
     */
    public void setYear(int year) {
        this.year = year;
    }

    public void readJson(JsonElement element, DataGrabberContext context) {
        DataTypeChecker checker = new DataTypeChecker(context.getCurrentFile());

        if (element.isJsonObject()) parseObject(element.getAsJsonObject(), checker);
        else parseString(element);
    }

    private void parseString(JsonElement element) {
        String stringDate = element.getAsString();

        if (!STRING_DATE_PATTERN.matcher(stringDate).matches()) throw new JsonParseException("Invalid date");

        String[] members = stringDate.split("-");

        this.year = Integer.parseInt(members[0]);
        this.month = Integer.parseInt(members[1]);
        this.day = Integer.parseInt(members[2]);
    }

    private void parseObject(JsonObject object, DataTypeChecker checker) {
        checker.searchExceptions(object, "year", RequiredDataType.INTEGER);
        checker.searchExceptions(object, "month", RequiredDataType.INTEGER);
        checker.searchExceptions(object, "day", RequiredDataType.INTEGER);

        this.day = object.get("day").getAsInt();
        this.year = object.get("year").getAsInt();
        this.month = object.get("month").getAsInt();
    }

    @Override
    public int compareTo(Date other) {
        int yearCompared;
        int monthCompared;
        int dayCompared;

        if (other instanceof DayDate dayDate) {
            yearCompared = year.compareTo(dayDate.year);
            monthCompared = month.compareTo(dayDate.month);
            dayCompared = day.compareTo(dayDate.day);
        } else {
            MonthDate monthDate = (MonthDate) other;
            yearCompared = year.compareTo(monthDate.getYear());
            monthCompared = month.compareTo(monthDate.getMonth());
            dayCompared = day.compareTo(0);
        }

        if (yearCompared == 0) {
            if (monthCompared == 0) {
                return dayCompared;
            } else return monthCompared;
        } else return yearCompared;
    }

    @Override
    public String toString() {
        return Text.translated("date.format", DateHelper.monthToString(month), DateHelper.getThing(day), year + "");
    }
}
