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

package dev.efekos.pg.process;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import dev.efekos.pg.Main;
import dev.efekos.pg.data.color.ColorTheme;
import dev.efekos.pg.data.color.ColorThemeManager;
import dev.efekos.pg.data.color.ColorThemeValue;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class LoadColorThemeProcess implements Process{
    @Override
    public String getName() {
        return "Load Color Theme";
    }

    @Override
    public void init(ProcessContext context) throws Exception {
        Main.LOGGER.info("Loading color theme");

        List<Field> colors = Arrays.stream(ColorTheme.class.getFields()).filter(field -> Arrays.asList(field.getType().getInterfaces()).contains(ColorThemeValue.class)).toList();

        Path colorThemeJsonPath = Path.of(context.dataPath, "color_theme.json");
        if(!Files.exists(colorThemeJsonPath)) throw new FileNotFoundException(colorThemeJsonPath.toString());
        String themeString = Files.readString(colorThemeJsonPath);
        JsonElement theme = JsonParser.parseString(themeString);
        if(!theme.isJsonObject()) throw new JsonSyntaxException("Expected an object inside 'color_theme.json'.");

        int total = 0;
        for (Field colorField : colors) {
            ColorThemeValue value = (ColorThemeValue) colorField.get(null);

            String[] keys = value.key().split("\\.");

            JsonElement finalThing = theme.getAsJsonObject();

            for (int i = 0; i < keys.length; i++) {
                String key = keys[i];
                if(!(finalThing.isJsonObject()&&finalThing.getAsJsonObject().has(key))){
                    if(i+1==keys.length) throw new JsonSyntaxException("Missing key '"+value.key()+"' in 'color_theme.json'");
                    else continue;
                }

                finalThing = finalThing.getAsJsonObject().get(key);
            }

            ColorThemeManager.putColor(value,value.read(finalThing));
            total++;
        }

        Main.LOGGER.success("Loaded "+total+" color theme values.");
    }
}
