package dev.efekos.pg.data.schema;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.RequiredDataType;

import java.util.regex.Pattern;

public class FullDate implements JsonSchema {
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
    public void readJson(JsonElement element, DataGrabberContext context) {
        DataTypeChecker checker = new DataTypeChecker(context.getCurrentFile());

        if(element.isJsonObject())parseObject(checker,element.getAsJsonObject());
        else parseString(element.getAsString());
    }

    private void parseString(String date) {

        if(!STRING_DATE_PATTERN.matcher(date).matches()) throw new JsonParseException("Invalid date");

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
}
