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
        this.context = new DataGrabberContext(mainPath + "\\data", this.mainPath);
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

    public TagColorInfo grabTagColorInfo() throws IOException{
        String path = mainPath + "\\tag_colors.json";

        String file = readFile(path);
        JsonElement element = JsonParser.parseString(file);

        TagColorInfo info = new TagColorInfo();
        context.setCurrentFile("tag_colors.json");
        info.readJson(element,context);
        return info;
    }


    public String grabMarkdownFile(String fileName) throws IOException {
        String file = readFile(mainPath + "\\" + fileName + ".md");
        System.out.println("Reading markdown: " + fileName.replaceAll("\\\\", "/"));

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
        String dirPathString = mainPath + "\\projects";
        Path dirPath = Path.of(dirPathString);

        if (!Files.exists(dirPath)) throw new FileNotFoundException(dirPathString);
        if (!Files.isDirectory(dirPath)) throw new NotDirectoryException(dirPathString);

        File projectsDir = new File(dirPathString);


        List<Project> projects = new ArrayList<>();
        for (File dir : projectsDir.listFiles()) {
            if (!Files.isDirectory(dir.toPath())) throw new NotDirectoryException(dir.getAbsolutePath());

            // log
            context.setCurrentFile(dir.getPath().replace(mainPath, ""));
            System.out.println("Grabbing directory: projects/" + dir.toPath().getFileName().toString());

            // main json
            String mainJson = readFile(dir.getPath() + "\\main.json");
            Project project = new Project();
            context.setCurrentFile(context.getCurrentFile() + "\\main.json");
            project.readJson(JsonParser.parseString(mainJson), context);

            // id,readme,license
            project.setId(dir.toPath().getFileName().toString());
            project.setFullLicense(grabMarkdownFile("projects\\" + project.getId() + "\\license"));

            // gallery
            ProjectGalleryImageList images = new ProjectGalleryImageList();
            context.setCurrentFile("projects/"+project.getId()+"/gallery.json");
            images.readJson(JsonParser.parseString(readFile(dir.getPath()+"\\gallery.json")),context);
            project.setGalleryImages(images);

            // icon
            Path iconPath = Path.of(mainPath, "projects", project.getId(), "icon.png");
            if (!Files.exists(iconPath)) throw new FileNotFoundException(iconPath.toString());

            // assets
            Path assetsPath = Path.of(mainPath, "projects", project.getId(), "assets");
            if (!Files.exists(assetsPath))
                throw new FileNotFoundException(assetsPath.toString());

            projects.add(project);
            System.out.println("Grabbed directory: projects/" + project.getId());
        }

        System.out.println("Grabbed directory: projects");
        return projects;
    }
}
