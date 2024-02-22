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

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dev.efekos.pg.resource.ResourceManager;
import dev.efekos.pg.resource.Resources;
import org.apache.commons.text.StringEscapeUtils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Text {
    public static String translated(String key, int nesting,String... arguments) {
        StringBuilder builder = new StringBuilder();

        builder.append("<span class=\"key\"")
                .append(" id=\"key-")
                .append(key)
                .append("\""); // <span class="key" id="key-$key"


        for (int i = 0; i < Arrays.asList(arguments).size(); i++) {
            builder.append(" arg").append(i).append("=\"").append(StringEscapeUtils.escapeHtml4(arguments[i])).append("\""); // this is the nesting I tried. just spamming quotes
        } // <span class="key" id="key-$key" arg0="$arguments[0]"... arg10="$arguments[10]"

        JsonElement defaultLang = JsonParser.parseString(ResourceManager.getResource(Resources.JSON_LANGUAGE_EN));

        builder.append(">");
        if(!defaultLang.getAsJsonObject().has(key)) throw new IllegalArgumentException("Unknown translation key: "+key);
        String defaultText = defaultLang.getAsJsonObject().get(key).getAsString();

        String result = Pattern.compile("\\{[0-9]+}").matcher(defaultText).replaceAll(matchResult -> {
            String s = matchResult.group();
            int i = Integer.parseInt(s.substring(1, s.length() - 1));
            if(Arrays.asList(arguments).size()<i+1)return s;
            else return arguments[i];
        });

        builder.append(result).append("</span>");

        return builder.toString();
    }

    public static String translated(String key,String... arguments){
        return translated(key,0,arguments);
    }

    private static String buildQuotes(int amount){
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < amount+1; i++) {
            builder.append("&quot;");
        }

        return builder.toString();
    }
}
