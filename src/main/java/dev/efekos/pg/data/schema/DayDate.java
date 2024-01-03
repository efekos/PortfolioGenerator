package dev.efekos.pg.data.schema;

import com.google.gson.JsonObject;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.RequiredDataType;

public class DayDate implements JsonSchema{
    private int day;
    private int month;
    private int year;

    public DayDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
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
