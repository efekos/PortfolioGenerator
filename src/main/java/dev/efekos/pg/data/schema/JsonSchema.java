package dev.efekos.pg.data.schema;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dev.efekos.pg.data.DataGrabberContext;

public interface JsonSchema {
    /**
     * Reads values from the element given.
     *
     * @param element A {@link JsonElement}.
     * @param context Current grabbing context for exception messages
     * @throws JsonParseException Depending on what class is this.
     */
    void readJson(JsonElement element, DataGrabberContext context) throws JsonParseException;
}
