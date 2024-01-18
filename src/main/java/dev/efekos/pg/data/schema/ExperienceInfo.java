package dev.efekos.pg.data.schema;

import com.google.gson.*;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.RequiredDataType;

import java.util.ArrayList;
import java.util.List;

public class ExperienceInfo implements JsonSchema {

    private List<ExperienceEntry> entries;
    private ExperienceEntry currentJob = null;

    public ExperienceInfo(List<ExperienceEntry> entries) {
        this.entries = entries;
    }

    public List<ExperienceEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<ExperienceEntry> entries) {
        this.entries = entries;
    }

    public ExperienceEntry getCurrentJob() {
        return currentJob;
    }

    public void setCurrentJob(ExperienceEntry currentJob) {
        if(!currentJob.isCurrentJob()) throw new RuntimeException("Is your brain okay dude you are literally passing in a non current job entry into fucking set current job method");
        this.currentJob = currentJob;
    }

    public boolean hasCurrentJob(){
        return currentJob!=null;
    }

    @Override
    public void readJson(JsonElement element, DataGrabberContext context) throws JsonParseException {
        DataTypeChecker checker = new DataTypeChecker(context.getCurrentFile());
        checker.expectArray(element);

        JsonArray array = element.getAsJsonArray();
        ArrayList<ExperienceEntry> experienceList = new ArrayList<>();

        for (JsonElement element1 : array) {
            if (!element1.isJsonObject()) throw new JsonSyntaxException("Elements in 'experience.json' must be an object");

            ExperienceEntry entry = new ExperienceEntry();
            entry.readJson(element1.getAsJsonObject(), context);

            if(entry.isCurrentJob()) {
                if(this.currentJob!=null) throw new JsonParseException("Only one entry can be current job");
                this.currentJob = entry;
            }

            experienceList.add(entry);
        }

        this.entries = experienceList;
    }
}
