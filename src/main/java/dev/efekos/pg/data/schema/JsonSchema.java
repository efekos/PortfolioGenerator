package dev.efekos.pg.data.schema;

import com.google.gson.JsonObject;
import dev.efekos.pg.data.DataGrabberContext;

public interface JsonSchema {
    void readJson(JsonObject object, DataGrabberContext context);
}
