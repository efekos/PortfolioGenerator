package dev.efekos.pg.output;

import dev.efekos.pg.Main;
import dev.efekos.pg.data.schema.GeneralInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileGenerator {

    private final String binPath;

    /**
     * Constructs a new {@link FileGenerator}
     * @param binPath Path to the output folder.
     */
    public FileGenerator(String binPath) {
        this.binPath = binPath;
    }

    /**
     * Generates a main <code>index.html</code> file, the home page for the entire website.
     * @param info Grabbed {@link GeneralInfo} to place its data to the page.
     * @throws IOException If something goes wrong
     */
    public void generateIndexFile(GeneralInfo info) throws IOException {
        System.out.println("Generating file: index.html");

        String fileString = Main.readStringResource("/site/index.html")
        .replaceAll("\\{name}",info.getName())
        .replaceAll("\\{title}",info.getTitle())
        .replaceAll("\\{welcomer}",info.getWelcomer());

        File file = new File(binPath + "\\index.html");

        file.createNewFile();

        FileWriter writer = new FileWriter(file);

        writer.write(fileString);
        writer.flush();
        writer.close();

        System.out.println("Generated file: index.html");
        copyFile("/site/style.css","\\style\\main_style.css");
    }

    public void copyFile(String resourceLocation,String outputLocation) throws IOException {
        System.out.println("Copying file: "+resourceLocation);

        String fileString = Main.readStringResource(resourceLocation);
        File file = new File(binPath+outputLocation);
        file.getParentFile().mkdirs();
        file.createNewFile();

        FileWriter writer = new FileWriter(file);
        writer.write(fileString);
        writer.flush();
        writer.close();

        System.out.println("Copied file: "+resourceLocation);
    }

    public void copyProfileImage(String mainPath) throws IOException{
        System.out.println("Moving file: profile.png");

        Path dataPath = Path.of(mainPath, "data", "profile.png");
        if(!Files.exists(dataPath)) throw new FileNotFoundException("Required file: profile.png");

        Files.createDirectory(Path.of(binPath,"images"));
        Files.copy(dataPath,Path.of(binPath,"images","profile.png"));

        System.out.println("Moved file: profile.png");
    }
}
