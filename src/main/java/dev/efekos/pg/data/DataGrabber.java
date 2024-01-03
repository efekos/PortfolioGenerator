package dev.efekos.pg.data;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import dev.efekos.pg.data.schema.GeneralInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DataGrabber {
    private final String mainPath;

    public DataGrabber(String mainPath) {
        this.mainPath = mainPath+"\\data";
    }

    public GeneralInfo grabGeneralInfo()throws IOException {
        Path path = Path.of(mainPath, "general.json");
        if(!Files.exists(path))throw new FileNotFoundException("'general.json' file missing.");
        String file = Files.readString(path);
        JsonElement element = JsonParser.parseString(file);
        if(!element.isJsonObject())throw new JsonSyntaxException("Bro ur 'general.json' file isn't even an object do you know how json files usually work");
        JsonObject object = element.getAsJsonObject();

        GeneralInfo generalInfo = new GeneralInfo();
        generalInfo.readJson(object);

        return generalInfo;
    }
}
