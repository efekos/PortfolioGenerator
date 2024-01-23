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
import java.util.Arrays;
import java.util.List;

public class ProjectGalleryImage implements JsonSchema{
    private String name;
    private String description;
    private String file;
    private String id;

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
        if(ALLOWED_IMAGE_TYPES.stream().noneMatch(this.file::contains)) throw new JsonParseException("Unsupported image type. Use png or jpeg");


        //id
        String[] split = file.split("/");
        this.id = split[split.length-1].replace(".","");
    }

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList("png","jpg","jpeg","PNG","JPEG","JPG");

    public ProjectGalleryImage() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
