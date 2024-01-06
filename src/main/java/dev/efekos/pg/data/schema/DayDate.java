package dev.efekos.pg.data.schema;

import com.google.gson.JsonObject;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.RequiredDataType;

/**
 * Represents a date that just needs a day, year, and month.
 */
public class DayDate implements JsonSchema{
    /**
     * Day of the date. Will be day of the month (such as 2nd July, but not Tuesday)
     */
    private int day;
    /**
     * Month of the date.
     */
    private int month;
    /**
     * Year of the date.
     */
    private int year;

    /**
     * Constructs a new {@link DayDate}
     * @param day Day of this date. Will be considered as day of the month (such as 2nd July, but not Tuesday).
     * @param month Month of this date.
     * @param year Year of this date.
     */
    public DayDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    /**
     * Returns the day of this date
     * @return Day value of this date
     */
    public int getDay() {
        return day;
    }

    /**
     * Changes the day of this date
     * @param day New day value
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * Returns month value of this date.
     * @return Month of this date.
     */
    public int getMonth() {
        return month;
    }

    /**
     * Changes the month of this date.
     * @param month Month of this date.
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * Returns the year of this date.
     * @return Year value of this date.
     */
    public int getYear() {
        return year;
    }

    /**
     * Changes the year value of this date.
     * @param year New year value.
     */
    public void setYear(int year) {
        this.year = year;
    }


    public void readJson(JsonObject object, DataGrabberContext context) {
        DataTypeChecker checker = new DataTypeChecker(context.getCurrentFile());
        checker.searchExceptions(object,"year", RequiredDataType.INTEGER);
        checker.searchExceptions(object,"month", RequiredDataType.INTEGER);
        checker.searchExceptions(object,"day", RequiredDataType.INTEGER);

        this.day = object.get("day").getAsInt();
        this.year = object.get("year").getAsInt();
        this.month = object.get("month").getAsInt();
    }
}
