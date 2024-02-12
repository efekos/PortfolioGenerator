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

package dev.efekos.pg.data.color;

import dev.efekos.pg.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorThemeManager {
    private static final Map<String, String> themeMap = new HashMap<>();

    public static void putColor(ColorThemeValue value, String s) {
        putColor(value.key(), s);
    }

    public static void putColor(String s, String s2) {
        themeMap.put(s, s2);
    }

    public static String get(ColorThemeValue value) {
        return themeMap.get(value.key());
    }

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("%%colorTheme\\.([A-Za-z]+(\\.[A-Za-z]+)+(:[A-Za-z]+)?)%%");

    public static String doReplacement(String string) {
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(string);

        return matcher.replaceAll(matchResult -> {
            String s = string.substring(matchResult.start(), matchResult.end());

            String input = s.substring(13, s.length() - 2);

            if (!themeMap.containsKey(input)) Main.LOGGER.devWarn("Missing color theme key: " + input);

            return themeMap.getOrDefault(input, "MISSING COLOR THEME KEY: " + input);
        });
    }
}
