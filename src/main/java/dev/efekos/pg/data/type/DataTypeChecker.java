package dev.efekos.pg.data.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import dev.efekos.pg.Main;

import java.lang.reflect.Method;

public class DataTypeChecker {

    private String currentFile;

    public DataTypeChecker(String currentFile) {
        this.currentFile = currentFile;
    }

    public DataTypeChecker(){
        this(Main.getMainPath().toString());
    }

    public String getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(String currentFile) {
        this.currentFile = currentFile;
    }

    public void searchExceptions(JsonObject object, String requiredKey, RequiredDataType type)throws JsonParseException{
        if(!object.has(requiredKey))throw new JsonSyntaxException("'"+requiredKey+"' missing in file '"+currentFile+"'");

        JsonElement element = object.get(requiredKey);
        switch (type){
            case ARRAY -> {
                if(!element.isJsonArray())throw new JsonSyntaxException("'"+requiredKey+"' must be an array.");
            }
            case DOUBLE -> {
                if(!element.isJsonPrimitive()) throw new JsonSyntaxException("'"+requiredKey+"' must be a double.");
            }
            case OBJECT -> {
                if(!element.isJsonObject()) throw new JsonSyntaxException("'"+requiredKey+"' must be an object.");
            }
            case STRING -> {
                if(!element.isJsonPrimitive()) throw new JsonSyntaxException("'"+requiredKey+"' must be a string.");
            }
            case BOOLEAN -> {
                if(!element.isJsonPrimitive()) throw new JsonSyntaxException("'"+requiredKey+"' must be a boolean");
            }
            case INTEGER -> {
                if(!element.isJsonPrimitive()) throw new JsonSyntaxException("'"+requiredKey+"' must be an integer");
            }
        }
    }
}
