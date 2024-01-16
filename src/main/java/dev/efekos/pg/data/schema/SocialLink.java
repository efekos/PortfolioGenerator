package dev.efekos.pg.data.schema;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.RequiredDataType;

public class SocialLink implements JsonSchema {
    private String display;
    private String url;
    private String icon;

    public SocialLink(String display, String url) {
        this.display = display;
        this.url = url;
    }

    @Override
    public void readJson(JsonElement element, DataGrabberContext context) {
        DataTypeChecker checker = new DataTypeChecker(context.getCurrentFile());
        checker.expectObject(element);
        JsonObject object = element.getAsJsonObject();

        checker.searchExceptions(object, "display", RequiredDataType.STRING);
        checker.searchExceptions(object, "url", RequiredDataType.STRING);
        checker.searchExceptions(object, "icon", RequiredDataType.STRING);

        this.display = object.get("display").getAsString();
        this.url = object.get("url").getAsString();
        this.icon = object.get("icon").getAsString();
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
