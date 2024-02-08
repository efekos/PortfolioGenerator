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

package dev.efekos.pg.data.schema;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.RequiredDataType;

public class JsonColor implements JsonSchema{
    private int red;
    private int green;
    private int blue;
    private String hex;
    private boolean isHex;

    @Override
    public String toString() {
        return isHex?hex:"rgb("+red+","+green+","+blue+")";
    }

    public static JsonColor from(JsonElement element, DataGrabberContext context){
        JsonColor color = new JsonColor();
        color.readJson(element,context);
        return color;
    }

    @Override
    public void readJson(JsonElement element, DataGrabberContext context) throws JsonParseException {
        if(element.isJsonObject()) readRgb(element.getAsJsonObject(),new DataTypeChecker(context.getCurrentFile()));
        else if (element.isJsonPrimitive()) {
            setHex(element.getAsString());
            setHex(true);
        };
    }

    private void readRgb(JsonObject object,DataTypeChecker checker) {
        checker.searchExceptions(object,"r", RequiredDataType.INTEGER);
        checker.searchExceptions(object,"g", RequiredDataType.INTEGER);
        checker.searchExceptions(object,"b", RequiredDataType.INTEGER);

        setHex(false);
        setRed(object.get("r").getAsInt());
        setGreen(object.get("g").getAsInt());
        setBlue(object.get("b").getAsInt());
    }

    public void setHex(boolean hex) {
        isHex = hex;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public boolean isHex() {
        return isHex;
    }
}
