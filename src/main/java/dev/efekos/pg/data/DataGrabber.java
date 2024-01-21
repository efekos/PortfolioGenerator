package dev.efekos.pg.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.efekos.pg.data.schema.*;
import dev.efekos.pg.util.Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.ArrayList;
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
     *
     * @param mainPath Main path of the app, note that the main directory this grabber will be using is the 'data'
     *                 directory inside this main path. You will likely get an {@link IOException} if this path isn't a
     *                 directory or doesn't exist.
     */
    public DataGrabber(String mainPath) {
        this.mainPath = mainPath + "\\data";
        this.context = new DataGrabberContext(mainPath + "\\data",this.mainPath);
    }

    /**
     * Grabs a {@link GeneralInfo} from the data folder.
     *
     * @return {@link GeneralInfo} that has been read from the json files.
     * @throws IOException If something goes wrong.
     */
    public GeneralInfo grabGeneralInfo() throws IOException {
        context.setCurrentFile("general.json");
        System.out.println("Grabbing file: general.json");

        String file = readFile(mainPath + "\\general.json");
        JsonElement element = JsonParser.parseString(file);

        GeneralInfo generalInfo = new GeneralInfo();
        generalInfo.readJson(element, context);

        generalInfo.setWelcomer(grabMarkdownFile("welcomer"));
        generalInfo.setBio(grabMarkdownFile("bio"));

        return generalInfo;
    }

    private String readFile(String p) throws IOException {
        Path path = Path.of(p);
        System.out.println("Reading file: " + path.getFileName());

        if (!Files.exists(path)) throw new FileNotFoundException("'" + path.getFileName().toFile() + "' file missing.");
        return Files.readString(path);
    }


    public String grabMarkdownFile(String fileName) throws IOException {
        String file = readFile(mainPath + "\\" + fileName + ".md");
        System.out.println("Reading markdown: " + fileName.replaceAll("\\\\","/"));

        return Utilities.markdownToHtml(file);
    }


    public EducationInfo grabEducationInfo() throws IOException {
        context.setCurrentFile("education.json");
        System.out.println("Grabbing file: education.json");

        String file = readFile(mainPath + "\\education.json");
        JsonElement element = JsonParser.parseString(file);

        EducationInfo info = new EducationInfo();
        info.readJson(element, context);
        return info;
    }

    public ExperienceInfo grabExperienceInfo() throws IOException {
        context.setCurrentFile("experience.json");
        System.out.println("Grabbing file: experience.json");


        String file = readFile(mainPath + "\\experience.json");
        JsonElement element = JsonParser.parseString(file);

        ExperienceInfo info = new ExperienceInfo(new ArrayList<>());
        info.readJson(element, context);
        return info;
    }

    public List<Certificate> grabCertificates() throws IOException {
        System.out.println("Grabbing directory: certificates");
        String dirPathString = mainPath + "\\certificates";
        Path dirPath = Path.of(dirPathString);

        if (!Files.exists(dirPath)) throw new FileNotFoundException(dirPathString);
        if (!Files.isDirectory(dirPath)) throw new NotDirectoryException(dirPathString);

        File dir = new File(dirPathString);

        File[] files = dir.listFiles((drir, name) -> name.endsWith(".json"));
        ArrayList<Certificate> certificates = new ArrayList<>();


        for (File file : files) {
            context.setCurrentFile(file.getPath().replace(mainPath, ""));

            String stringJson = readFile(file.getPath());
            JsonElement element = JsonParser.parseString(stringJson);
            JsonObject object = element.getAsJsonObject();

            Certificate certificate = new Certificate();
            certificate.readJson(object, context);

            certificates.add(certificate);
        }

        return certificates;
    }

    public List<Project> grabProjects() throws IOException {
        System.out.println("Grabbing directory: projects");
        String dirPathString = mainPath+"\\projects";
        Path dirPath = Path.of(dirPathString);

        if(!Files.exists(dirPath)) throw new FileNotFoundException(dirPathString);
        if(!Files.isDirectory(dirPath)) throw new NotDirectoryException(dirPathString);

        File dir = new File(dirPathString);


        List<Project> projects = new ArrayList<>();
        for (File file : dir.listFiles()) {
            if(!Files.isDirectory(file.toPath())) throw new NotDirectoryException(file.getAbsolutePath());

            // log
            context.setCurrentFile(file.getPath().replace(mainPath, ""));
            System.out.println("Grabbing directory: projects/"+file.toPath().getFileName().toString());

            // main json
            String mainJson = readFile(file.getPath()+"\\main.json");
            Project project = new Project();
            context.setCurrentFile(context.getCurrentFile()+"\\main.json");
            project.readJson(JsonParser.parseString(mainJson),context);

            // id,readme,license
            project.setId(file.toPath().getFileName().toString());
            project.setReadmeFile(grabMarkdownFile("projects\\"+project.getId()+"\\readme"));
            project.setFullLicense(grabMarkdownFile("projects\\"+project.getId()+"\\license"));

            // icon
            Path iconPath = Path.of(mainPath, "projects",project.getId(), "icon.png");
            if(!Files.exists(iconPath)) throw new FileNotFoundException(iconPath.toString());

            projects.add(project);
            System.out.println("Grabbed directory: projects/"+project.getId());
        }

        System.out.println("Grabbed directory: projects");
        return projects;
    }
}
