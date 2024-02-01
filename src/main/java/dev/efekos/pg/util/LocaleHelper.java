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

package dev.efekos.pg.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.efekos.pg.Main;
import dev.efekos.pg.resource.ResourceManager;
import dev.efekos.pg.resource.Resources;

import java.util.HashMap;
import java.util.Map;

public class LocaleHelper {
    private static final Map<String, Locale> localeList = new HashMap<>();

    public static void loadLocales() {
        Main.LOGGER.info("Loading locales...");
        String jsonString = ResourceManager.getResource(Resources.JSON_VALID_LOCALES);
        JsonObject element = JsonParser.parseString(jsonString).getAsJsonObject();

        localeList.clear();

        element.asMap().forEach((code, value) -> {
            Main.DEBUG_LOGGER.info("Loading locale code: " + code);
            JsonObject object = value.getAsJsonObject();
            String name = object.get("name").getAsString();
            String nativeName = object.get("nativeName").getAsString();

            localeList.put(code, new Locale(code, name, nativeName));
        });

        Main.DEBUG_LOGGER.info("Final locale code count: " + localeList.size());
        Main.LOGGER.success("Loaded locales");
    }

    public static Locale getLocale(String code) {
        return localeList.getOrDefault(code, localeList.get("en"));
    }

    public static boolean isNotValid(String code) {
        return !localeList.containsKey(code);
    }
}
