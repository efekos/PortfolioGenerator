package dev.efekos.pg.data.schema;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.RequiredDataType;

import java.util.regex.Pattern;

public class SocialLink implements JsonSchema {
    private String display;
    private String url;
    private String icon;

    public SocialLink(String display, String url) {
        this.display = display;
        this.url = url;
    }

    @Override
    public void readJson(JsonObject object, DataGrabberContext context) {
        DataTypeChecker checker = new DataTypeChecker(context.getCurrentFile());
        checker.searchExceptions(object, "display", RequiredDataType.STRING);
        checker.searchExceptions(object, "url", RequiredDataType.STRING);
        checker.searchExceptions(object, "icon", RequiredDataType.STRING);

        this.display = object.get("display").getAsString();
        this.url = object.get("url").getAsString();
        this.icon = object.get("icon").getAsString();
    }
}
