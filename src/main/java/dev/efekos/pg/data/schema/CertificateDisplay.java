package dev.efekos.pg.data.schema;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.efekos.pg.Main;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.RequiredDataType;

public class CertificateDisplay implements JsonSchema{
    private String image;
    private String title;
    private String description;

    public CertificateDisplay(String image, String title, String description) {
        this.image = image;
        this.title = title;
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void readJson(JsonObject object, DataGrabberContext context) throws JsonParseException {
        DataTypeChecker checker = new DataTypeChecker(context.getCurrentFile());

        checker.searchExceptions(object,"image", RequiredDataType.STRING);
        checker.searchExceptions(object,"title", RequiredDataType.STRING);
        checker.searchExceptions(object,"description", RequiredDataType.STRING);

        setImage(Main.getMainPath()+"\\data\\certificates\\"+object.get("image").getAsString().replaceAll("/","\\"));
        setTitle(object.get("title").getAsString());
        setDescription(object.get("description").getAsString());
    }
}
