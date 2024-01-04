package dev.efekos.pg.output;

import dev.efekos.pg.Main;
import dev.efekos.pg.data.schema.GeneralInfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileGenerator {

    public void generateIndexFile(GeneralInfo info,String binPath) throws IOException {
        String fileString = Main.readStringResource("/site/index.html");
        fileString = fileString.replaceAll("\\{name}",info.getName());
        fileString = fileString.replaceAll("\\{title}",info.getTitle());

        File file = new File(binPath + "\\index.html");

        file.createNewFile();

        FileWriter writer = new FileWriter(file);

        writer.write(fileString);
        writer.flush();
        writer.close();
    }
}
