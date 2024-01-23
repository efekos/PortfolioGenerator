package dev.efekos.pg.data.schema;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.RequiredDataType;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ProjectGalleryImage implements JsonSchema{
    private String name;
    private String description;
    private String file;
    
    @Override
    public void readJson(JsonElement element, DataGrabberContext context) throws JsonParseException {
        DataTypeChecker checker = new DataTypeChecker(context.getCurrentFile());
        
        checker.expectObject(element);
        JsonObject object = element.getAsJsonObject();

        checker.searchExceptions(object,"name", RequiredDataType.STRING);
        checker.searchExceptions(object,"description", RequiredDataType.STRING);
        checker.searchExceptions(object,"file", RequiredDataType.STRING);

        this.name = object.get("name").getAsString();
        this.description = object.get("description").getAsString();
        this.file = object.get("file").getAsString().replaceAll("\\\\","/");
    }

    public ProjectGalleryImage() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}