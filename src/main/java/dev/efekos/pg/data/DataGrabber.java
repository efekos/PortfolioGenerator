package dev.efekos.pg.data;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import dev.efekos.pg.data.schema.GeneralInfo;
import dev.efekos.pg.util.Utilities;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DataGrabber {
    private final String mainPath;
    private final DataGrabberContext context;

    public DataGrabber(String mainPath) {
        System.out.println("Starting data grab process");
        this.mainPath = mainPath+"\\data";
        this.context =  new DataGrabberContext(mainPath+"\\data");
    }

    public GeneralInfo grabGeneralInfo()throws IOException {
        context.setCurrentFile("general.json");
        System.out.println("Grabbing file: general.json");

        Path path = Path.of(mainPath, "general.json");
        if(!Files.exists(path))throw new FileNotFoundException("'general.json' file missing.");
        String file = Files.readString(path);
        JsonElement element = JsonParser.parseString(file);
        if(!element.isJsonObject())throw new JsonSyntaxException("Bro ur 'general.json' file isn't even an object do you know how json files usually work");
        JsonObject object = element.getAsJsonObject();

        GeneralInfo generalInfo = new GeneralInfo();
        generalInfo.readJson(object,context);

        return generalInfo;
    }

    private String readFile(String p) throws IOException{
        Path path = Path.of(p);
        System.out.println("Reading file: "+path.getFileName());

        if(!Files.exists(path))throw new FileNotFoundException("'"+path.getFileName().toFile()+"' file missing.");
        return Files.readString(path);
    }

    public String grabMarkdownFile(String fileName) throws IOException{
        String file = readFile(mainPath + "\\" + fileName + ".md");
        System.out.println("Reading markdown: "+fileName);

        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(parser.parse(file));
    }
}
