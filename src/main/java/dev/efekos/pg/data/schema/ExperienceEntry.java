package dev.efekos.pg.data.schema;

import com.google.gson.JsonElement;
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
    private boolean currentJob;

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

    public boolean isCurrentJob() {
        return currentJob;
    }

    public void setCurrentJob(boolean currentJob) {
        this.currentJob = currentJob;
    }

    @Override
    public void readJson(JsonElement element, DataGrabberContext context) throws JsonParseException {
        DataTypeChecker checker = new DataTypeChecker(context.getCurrentFile());
        checker.expectObject(element);
        JsonObject object = element.getAsJsonObject();

        checker.searchExceptions(object, "company", RequiredDataType.STRING);
        checker.searchExceptions(object, "position", RequiredDataType.STRING);

        this.company = object.get("company").getAsString();
        this.position = object.get("position").getAsString();


        if (object.has("current_job")) {

            checker.searchExceptions(object,"current_job",RequiredDataType.BOOLEAN);

            this.currentJob = object.get("current_job").getAsBoolean();

        } else {
            this.currentJob = false;
        }

        if(!currentJob){
            MonthDate fromDate = new MonthDate(0, 0);
            fromDate.readJson(object.get("from"), context);

            MonthDate toDate = new MonthDate(0, 0);
            toDate.readJson(object.get("to"), context);

            this.from = fromDate;
            this.to = toDate;
        }

    }
}