package dev.efekos.pg.data.schema;

import com.google.gson.JsonObject;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.RequiredDataType;

public class FullDate implements JsonSchema{
    private int day;
    private int month;
    private int year;
    private int minute;
    private int hour;
    private int second;

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
    public void readJson(JsonObject object, DataGrabberContext context) {
        DataTypeChecker checker = new DataTypeChecker();
        checker.setCurrentFile(context.getCurrentFile());

        checker.searchExceptions(object,"day", RequiredDataType.INTEGER);
        checker.searchExceptions(object,"month", RequiredDataType.INTEGER);
        checker.searchExceptions(object,"year", RequiredDataType.INTEGER);
        checker.searchExceptions(object,"minute", RequiredDataType.INTEGER);
        checker.searchExceptions(object,"hour", RequiredDataType.INTEGER);
        checker.searchExceptions(object,"second", RequiredDataType.INTEGER);

        this.day = object.get("day").getAsInt();
        this.month = object.get("month").getAsInt();
        this.year = object.get("year").getAsInt();
        this.minute = object.get("minute").getAsInt();
        this.hour = object.get("hour").getAsInt();
        this.second = object.get("second").getAsInt();
    }
}
