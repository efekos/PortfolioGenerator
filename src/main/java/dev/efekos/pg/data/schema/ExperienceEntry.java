package dev.efekos.pg.data.schema;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.RequiredDataType;

public class ExperienceEntry implements JsonSchema {
    private String company;
    private String position;
    private MonthDate from;
    private MonthDate to;

    public ExperienceEntry(String company, String position, MonthDate from, MonthDate to) {
        this.company = company;
        this.position = position;
        this.from = from;
        this.to = to;
    }

    public ExperienceEntry() {

    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public MonthDate getFrom() {
        return from;
    }

    public void setFrom(MonthDate from) {
        this.from = from;
    }

    public MonthDate getTo() {
        return to;
    }

    public void setTo(MonthDate to) {
        this.to = to;
    }

    @Override
    public void readJson(JsonObject object, DataGrabberContext context) throws JsonParseException {
        DataTypeChecker checker = new DataTypeChecker(context.getCurrentFile());

        checker.searchExceptions(object, "company", RequiredDataType.STRING);
        checker.searchExceptions(object, "position", RequiredDataType.STRING);
        checker.searchExceptions(object, "from", RequiredDataType.OBJECT);
        checker.searchExceptions(object, "to", RequiredDataType.OBJECT);

        this.company = object.get("company").getAsString();
        this.position = object.get("position").getAsString();


        MonthDate fromDate = new MonthDate(0, 0);
        fromDate.readJson(object.get("from").getAsJsonObject(), context);

        MonthDate toDate = new MonthDate(0, 0);
        toDate.readJson(object.get("to").getAsJsonObject(), context);

        this.from = fromDate;
        this.to = toDate;
    }
}