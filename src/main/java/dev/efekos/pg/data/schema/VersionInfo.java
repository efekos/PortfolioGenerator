package dev.efekos.pg.data.schema;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.RequiredDataType;
import dev.efekos.pg.data.type.VersionInfoType;
import dev.efekos.pg.data.type.VersionType;

import java.util.ArrayList;
import java.util.List;

public class VersionInfo implements JsonSchema{

    private VersionInfoType type;
    private List<Version> versions = new ArrayList<>();
    private String file;

    @Override
    public void readJson(JsonElement element, DataGrabberContext context) throws JsonParseException {
        DataTypeChecker checker = new DataTypeChecker(context.getCurrentFile());

        checker.expectObject(element);
        JsonObject object = element.getAsJsonObject();

        checker.searchExceptions(object,"type", RequiredDataType.STRING);

        try {
            this.type = VersionInfoType.valueOf(object.get("type").getAsString());
        } catch (IllegalArgumentException e) {
            throw new JsonParseException("Unknown version info type '"+object.get("type").getAsString()+"'");
        }

        if (type == VersionInfoType.object) {
            checker.searchExceptions(object, "value", RequiredDataType.ARRAY);

            JsonArray linkArray = object.getAsJsonArray("value");

            for (JsonElement linkElement : linkArray) {
                Version version = new Version(new DayDate(0, 0, 0), "", VersionType.BETA);
                version.readJson(linkElement, context);
                versions.add(version);
            }
        } else {
            checker.searchExceptions(object, "value", RequiredDataType.STRING);
            this.file = object.get("value").getAsString();
        }
    }

    public VersionInfoType getType() {
        return type;
    }

    public void setType(VersionInfoType type) {
        this.type = type;
    }

    public List<Version> getVersions() {
        return versions;
    }

    public void setVersions(List<Version> versions) {
        this.versions = versions;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
