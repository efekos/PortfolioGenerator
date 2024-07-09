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
import com.google.gson.JsonSyntaxException;
import dev.efekos.pg.Main;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.timeline.AchieveCertificateEvent;
import dev.efekos.pg.data.timeline.TimelineEvent;
import dev.efekos.pg.data.timeline.TimelineEventSource;
import dev.efekos.pg.data.type.CertificateType;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.RequiredDataType;
import lombok.Data;

import java.util.*;

@Data
public class Certificate implements JsonSchema, TimelineEventSource {
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList("png", "jpg", "jpeg", "pdf");

    private Map<String, String> images;
    private DayDate when;
    private CertificateType certificateType;
    private CertificateDisplay display;

    public Certificate() {
    }

    @Override
    public List<TimelineEvent> getEvents() {
        return List.of(
                new AchieveCertificateEvent(when, display.getTitle(), certificateType)
        );
    }

    @Override
    public void readJson(JsonElement element, DataGrabberContext context) throws JsonParseException {
        DataTypeChecker checker = new DataTypeChecker(context.getCurrentFile());
        checker.expectObject(element);
        JsonObject object = element.getAsJsonObject();

        checker.searchExceptions(object, "images", RequiredDataType.OBJECT);
        checker.searchExceptions(object, "type", RequiredDataType.STRING);
        checker.searchExceptions(object, "display", RequiredDataType.OBJECT);

        // type
        Optional<CertificateType> type = Arrays.stream(CertificateType.values()).filter(t -> t.getId().equalsIgnoreCase(object.get("type").getAsString())).findFirst();
        if (type.isEmpty())
            throw new JsonParseException("Unknown certificate type '" + object.get("type").getAsString() + "'");

        setCertificateType(type.get());

        // when
        DayDate date = new DayDate(0, 0, 0);
        date.readJson(object.get("when"), context);
        setWhen(date);

        // display
        CertificateDisplay certificateDisplay = new CertificateDisplay("", "", "");
        certificateDisplay.readJson(object.get("display"), context);
        setDisplay(certificateDisplay);

        // images
        Map<String, JsonElement> imagesMap = object.get("images").getAsJsonObject().asMap();
        List<String> seenTypeKeys = new ArrayList<>();
        this.images = new HashMap<>();

        imagesMap.forEach((typeKey, jsonElement) -> {
            if (!ALLOWED_IMAGE_TYPES.contains(typeKey.toLowerCase(Locale.ROOT)))
                throw new JsonParseException("Image type '" + typeKey + "' not allowed");
            if (seenTypeKeys.contains(typeKey.toLowerCase(Locale.ROOT)))
                throw new JsonParseException("Already saw image type '" + typeKey + "'");
            seenTypeKeys.add(typeKey);

            if (!jsonElement.isJsonPrimitive()) throw new JsonSyntaxException("Values of 'images' must be string");

            String path = toImagePath(jsonElement.getAsString());
            this.images.put(typeKey, path);

        });
    }

    private String toImagePath(String path) {
        return Main.getMainPath() + "\\data\\certificates\\" + path.replaceAll("/", "\\\\");
    }

}
