package dev.efekos.pg.data.schema;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.type.DataTypeChecker;

import java.util.HashMap;
import java.util.Map;

public class TagColorInfo implements JsonSchema{

    private final Map<String,String> colors = new HashMap<>();

    @Override
    public void readJson(JsonElement element, DataGrabberContext context) throws JsonParseException {
        DataTypeChecker checker = new DataTypeChecker(context.getCurrentFile());
        checker.expectObject(element);
        element.getAsJsonObject().asMap().forEach((key,jsonElement)-> colors.put(key,jsonElement.getAsString()));
    }

    public Map<String, String> getColors() {
        return colors;
    }

    public String getColor(String key){
        return colors.getOrDefault(key,"#FFFFFF");
    }
}
