package dev.efekos.pg.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.efekos.pg.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocaleHelper {
    private static final Map<String,Locale> localeList = new HashMap<>();

    public static void loadLocales() throws IOException {
        String jsonString = Main.readStringResource("/valid_locales.json");
        JsonObject element = JsonParser.parseString(jsonString).getAsJsonObject();

        localeList.clear();

        element.asMap().forEach((code, value) -> {
            JsonObject object = value.getAsJsonObject();
            String name = object.get("name").getAsString();
            String nativeName = object.get("nativeName").getAsString();

            localeList.put(code,new Locale(code,name,nativeName));
        });
    }

    public static Locale getLocale(String code){
        return localeList.getOrDefault(code,localeList.get("en"));
    }

    public static boolean isValid(String code){
        return localeList.containsKey(code);
    }
}
