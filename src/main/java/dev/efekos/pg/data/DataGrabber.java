package dev.efekos.pg.data;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import dev.efekos.pg.data.schema.EducationInfo;
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

/**
 * Used for making data grab process look clean in {@link dev.efekos.pg.Main}. Contains methods to grab different kinds of data from json files.
 */
public class DataGrabber {
    /**
     * Main path that the program is running.
     */
    private final String mainPath;
    /**
     * Current context to put in readJson methods.
     */
    private final DataGrabberContext context;

    /**
     * Constructs a new {@link DataGrabber}
     * @param mainPath Main path of the app, note that the main directory this grabber will be using is the 'data'
     *                 directory inside this main path. You will likely get an {@link IOException} if this path isn't a
     *                 directory or doesn't exist.
     */
    public DataGrabber(String mainPath) {
        System.out.println("Starting data grab process");
        this.mainPath = mainPath+"\\data";
        this.context =  new DataGrabberContext(mainPath+"\\data");
    }

    /**
     * Grabs a {@link GeneralInfo} from the data folder.
     * @return {@link GeneralInfo} that has been read from the json files.
     * @throws IOException If something goes wrong.
     */
    public GeneralInfo grabGeneralInfo()throws IOException {
        context.setCurrentFile("general.json");
        System.out.println("Grabbing file: general.json");

        String file = readFile(mainPath+"\\general.json");
        JsonElement element = JsonParser.parseString(file);
        if(!element.isJsonObject())throw new JsonSyntaxException("'general.json' not object");
        JsonObject object = element.getAsJsonObject();

        GeneralInfo generalInfo = new GeneralInfo();
        generalInfo.readJson(object,context);

        generalInfo.setWelcomer(grabMarkdownFile("welcomer"));

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


    public EducationInfo grabEducationInfo()throws IOException{
        context.setCurrentFile("education.json");
        System.out.println("Grabbing file: education.json");

        String file = readFile(mainPath + "\\education.json");
        JsonElement element = JsonParser.parseString(file);
        if(!element.isJsonObject()) throw new JsonSyntaxException("'general.json' not object");
        JsonObject object = element.getAsJsonObject();

        EducationInfo info = new EducationInfo();
        info.readJson(object,context);
        return info;
    }
}
