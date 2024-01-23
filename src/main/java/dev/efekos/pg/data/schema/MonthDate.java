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