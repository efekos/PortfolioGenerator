package dev.efekos.pg.data.schema;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.efekos.pg.data.DataGrabberContext;

public interface JsonSchema {
    /**
     * Reads values from the object given.
     * @param object A {@link JsonObject}.
     * @param context Current grabbing context for exception messages
     * @throws JsonParseException Depending on what class is this.
     */
    void readJson(JsonObject object, DataGrabberContext context) throws JsonParseException;
}
