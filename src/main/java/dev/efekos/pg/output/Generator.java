package dev.efekos.pg.output;

import dev.efekos.pg.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public interface Generator {
    default String makeId(String id){
        return id
                .replaceAll(" ","_")
                .toLowerCase(Locale.ROOT);
    }

    default String makeIdForLink(String id){
        return id
                .replaceAll(" ","_")
                .replaceAll("#","%23")
                .toLowerCase(Locale.ROOT);
    }

    default void copyResource(String resourceLocation, String outputLocation,String binPath) throws IOException {
        System.out.println("Copying file: " + resourceLocation);

        String fileString = Main.readStringResource(resourceLocation);
        File file = new File(binPath + outputLocation);
        file.getParentFile().mkdirs();
        file.createNewFile();

        FileWriter writer = new FileWriter(file);
        writer.write(fileString);
        writer.flush();
        writer.close();

        System.out.println("Copied file: " + resourceLocation);
    }

    default void writeFile(String path, String content) throws IOException {
        File file = new File(path);

        file.createNewFile();

        FileWriter writer = new FileWriter(file);

        writer.write(content);
        writer.flush();
        writer.close();
    }
}
