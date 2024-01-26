/*
 * Copyright 2024 efekos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the “Software”), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package dev.efekos.pg.data.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import dev.efekos.pg.Main;

public class DataTypeChecker {

    private final String currentFile;

    public DataTypeChecker(String currentFile) {
        this.currentFile = currentFile;
    }

    public DataTypeChecker() {
        this(Main.getMainPath().toString());
    }

    public String getCurrentFile() {
        return currentFile;
    }

    public void searchExceptions(JsonObject object, String requiredKey, RequiredDataType type) throws JsonParseException {
        if (!object.has(requiredKey))
            throw new JsonSyntaxException("'" + requiredKey + "' missing in file '" + currentFile + "'");

        JsonElement element = object.get(requiredKey);
        switch (type) {
            case ARRAY -> {
                if (!element.isJsonArray())
                    throw new JsonSyntaxException("'" + requiredKey + "' must be an array in file '" + currentFile + "'");
            }
            case DOUBLE -> {
                if (!element.isJsonPrimitive())
                    throw new JsonSyntaxException("'" + requiredKey + "' must be a double in file '" + currentFile + "'");
            }
            case OBJECT -> {
                if (!element.isJsonObject())
                    throw new JsonSyntaxException("'" + requiredKey + "' must be an object in file '" + currentFile + "'");
            }
            case STRING -> {
                if (!element.isJsonPrimitive())
                    throw new JsonSyntaxException("'" + requiredKey + "' must be a string in file '" + currentFile + "'");
            }
            case BOOLEAN -> {
                if (!element.isJsonPrimitive())
                    throw new JsonSyntaxException("'" + requiredKey + "' must be a boolean in file '" + currentFile + "'");
            }
            case INTEGER -> {
                if (!element.isJsonPrimitive())
                    throw new JsonSyntaxException("'" + requiredKey + "' must be an integer in file '" + currentFile + "'");
            }
        }
    }

    public void expectObject(JsonElement element) throws JsonParseException {
        if (!element.isJsonObject()) throw new JsonSyntaxException("'" + currentFile + "' must be an object");
    }

    public void expectArray(JsonElement element) throws JsonParseException {
        if (!element.isJsonArray()) throw new JsonSyntaxException("'" + currentFile + "' must be an array");
    }
}
