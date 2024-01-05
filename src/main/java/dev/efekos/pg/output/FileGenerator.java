package dev.efekos.pg.output;

import dev.efekos.pg.Main;
import dev.efekos.pg.data.schema.GeneralInfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileGenerator {

    public FileGenerator() {
        System.out.println("Starting file generate process");
    }

    public void generateIndexFile(GeneralInfo info, String binPath) throws IOException {
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
}
