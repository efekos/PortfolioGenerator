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

package dev.efekos.pg.data.color;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.efekos.pg.data.type.DataTypeChecker;

import java.util.ArrayList;
import java.util.List;

public record FontFamily(String key) implements ColorThemeValue {
    @Override
    public String read(JsonElement element) {
        DataTypeChecker checker = new DataTypeChecker("color_theme.json");

        checker.expectArray(element);

        JsonArray array = element.getAsJsonArray();

        List<String> fonts = new ArrayList<>();

        for (JsonElement jsonElement : array) {
            fonts.add(jsonElement.getAsString());
        }

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < fonts.size(); i++) {
            builder.append("'");
            builder.append(fonts.get(i));
            builder.append("'");
            if (i + 1 != fonts.size()) builder.append(", ");
        }

        return builder.toString();
    }
}
