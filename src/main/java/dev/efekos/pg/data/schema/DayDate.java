package dev.efekos.pg.data.schema;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.RequiredDataType;

import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * Represents a date that just needs a day, year, and month.
 */
public class DayDate implements JsonSchema,Comparable<DayDate> {
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

        if(element.isJsonObject()) parseObject(element.getAsJsonObject(),checker);
        else parseString(element);
    }

    private void parseString(JsonElement element) {
        String stringDate = element.getAsString();

        if(!STRING_DATE_PATTERN.matcher(stringDate).matches()) throw new JsonParseException("Invalid date");

        String[] members = stringDate.split("-");

        this.year = Integer.parseInt(members[0]);
        this.month = Integer.parseInt(members[1]);
        this.day = Integer.parseInt(members[2]);
    }

    private final Pattern STRING_DATE_PATTERN = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");

    private void parseObject(JsonObject object, DataTypeChecker checker) {
        checker.searchExceptions(object, "year", RequiredDataType.INTEGER);
        checker.searchExceptions(object, "month", RequiredDataType.INTEGER);
        checker.searchExceptions(object, "day", RequiredDataType.INTEGER);

        this.day = object.get("day").getAsInt();
        this.year = object.get("year").getAsInt();
        this.month = object.get("month").getAsInt();
    }

    @Override
    public int compareTo(DayDate other) {
        int yearc = year.compareTo(other.year);
        int monthc = month.compareTo(other.month);
        int dayc = day.compareTo(other.day);

        if(yearc==0){
            if(monthc==0){
                return dayc;
            } else return monthc;
        } else return yearc;
    }
}
