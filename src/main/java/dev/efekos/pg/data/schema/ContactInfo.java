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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.RequiredDataType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ContactInfo implements JsonSchema {

    private String email;
    private String number;
    private boolean includeSocials;
    private List<Place> places = new ArrayList<>();

    @Override
    public void readJson(JsonElement element, DataGrabberContext context) throws JsonParseException {
        DataTypeChecker checker = new DataTypeChecker(context.getCurrentFile());
        checker.expectObject(element);
        JsonObject object = element.getAsJsonObject();

        checker.searchExceptions(object, "email", RequiredDataType.STRING);
        checker.searchExceptions(object, "number", RequiredDataType.STRING);
        checker.searchExceptions(object, "socials", RequiredDataType.BOOLEAN);
        checker.searchExceptions(object, "places", RequiredDataType.ARRAY);

        this.email = object.get("email").getAsString();
        this.number = object.get("number").getAsString();
        this.includeSocials = object.get("socials").getAsBoolean();

        this.places.clear();
        JsonArray array = object.getAsJsonArray("places");
        for (JsonElement placeElement : array) {
            Place place = new Place();
            place.readJson(placeElement, context);
            this.places.add(place);
        }
    }

    public ContactInfo() {
    }

}
