/*
 * Copyright 2025 efekos
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

package dev.efekos.pg.data.schema;

import com.google.gson.*;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.RequiredDataType;
import dev.efekos.pg.resource.ResourceManager;
import dev.efekos.pg.resource.Resources;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Setter
@Getter
public class JsonColor implements JsonSchema {
    private static final List<String> cssColors = new ArrayList<>();
    private static boolean didLoadCssColors;
    private int red;
    private int green;
    private int blue;
    private String hexString;
    private boolean isHex;

    public JsonColor() {
        if (didLoadCssColors) return;
        didLoadCssColors = true;

        String resource = ResourceManager.getResource(Resources.JSON_CSS_COLOR_NAMES);
        JsonElement array = JsonParser.parseString(resource);

        for (JsonElement element : array.getAsJsonArray()) {
            cssColors.add(element.getAsString());
        }
    }

    public static JsonColor from(JsonElement element, DataGrabberContext context) {
        JsonColor color = new JsonColor();
        color.readJson(element, context);
        return color;
    }

    @Override
    public String toString() {
        return isHex ? hexString : "rgb(" + red + "," + green + "," + blue + ")";
    }

    @Override
    public void readJson(JsonElement element, DataGrabberContext context) throws JsonParseException {
        if (element.isJsonObject()) readRgb(element.getAsJsonObject(), new DataTypeChecker(context.getCurrentFile()));
        else if (element.isJsonPrimitive()) {

            if (!Pattern.compile("^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$").matcher(element.getAsString()).matches()) {
                if (!cssColors.contains(element.getAsString()))
                    throw new JsonParseException("Unknown color: " + element.getAsString());
            }
            setHexString(element.getAsString());
            setHex(true);

        } else throw new JsonSyntaxException("Expected a string or an object, got " + element + ".");
    }

    private void readRgb(JsonObject object, DataTypeChecker checker) {
        checker.searchExceptions(object, "r", RequiredDataType.INTEGER);
        checker.searchExceptions(object, "g", RequiredDataType.INTEGER);
        checker.searchExceptions(object, "b", RequiredDataType.INTEGER);

        setHex(false);
        setRed(object.get("r").getAsInt());
        setGreen(object.get("g").getAsInt());
        setBlue(object.get("b").getAsInt());
    }

}
