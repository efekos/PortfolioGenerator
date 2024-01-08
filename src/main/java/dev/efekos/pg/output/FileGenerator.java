package dev.efekos.pg.output;

import dev.efekos.pg.Main;
import dev.efekos.pg.data.schema.GeneralInfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
    }

    public void putImageFiles(MainImageFileInfo info,){}
}
