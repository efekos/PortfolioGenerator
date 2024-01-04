package dev.efekos.pg;

import dev.efekos.pg.data.DataGrabber;
import dev.efekos.pg.data.schema.GeneralInfo;
import dev.efekos.pg.output.FileGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    private static String MAIN_PATH;

    public static Path getMainPath() {
        return Path.of(MAIN_PATH);
    }

    public static void main(String[] args)throws Exception {
        MAIN_PATH = System.getProperty("user.dir");

        DataGrabber grabber = new DataGrabber(MAIN_PATH);

        // data grabbing
        GeneralInfo generalInfo = grabber.grabGeneralInfo();

        // bin
        String binPath = MAIN_PATH+"\\bin";
        Path path = Path.of(binPath);
        Files.deleteIfExists(path);

        // generating
        FileGenerator generator = new FileGenerator();
        Files.createDirectory(path);

        generator.generateIndexFile(generalInfo,binPath);
    }

    public static String readStringResource(String path) throws IOException {
        InputStream stream = Main.class.getResource(path).openStream();
        return new String(stream.readAllBytes());
    }
}