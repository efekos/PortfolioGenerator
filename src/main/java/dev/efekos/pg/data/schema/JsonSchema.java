package dev.efekos.pg.data.schema;

import com.google.gson.JsonObject;

public interface JsonSchema {
    void readJson(JsonObject object);
}
