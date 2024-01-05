package dev.efekos.pg;

import dev.efekos.pg.data.DataGrabber;
import dev.efekos.pg.data.schema.GeneralInfo;
import dev.efekos.pg.output.FileGenerator;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static String MAIN_PATH;

    public static Path getMainPath() {
        return Path.of(MAIN_PATH);
    }

    public static void main(String[] args)throws Exception {
        MAIN_PATH = System.getProperty("user.dir");
        System.out.println("Hello World!");


        DataGrabber grabber = new DataGrabber(MAIN_PATH);

        // data grabbing
        GeneralInfo generalInfo = grabber.grabGeneralInfo();
        generalInfo.setWelcomer(grabber.grabMarkdownFile("welcomer"));

        System.out.println("Data grab process ended successfully");

        // bin
        System.out.println("Refreshing bin");
        String binPathString = MAIN_PATH+"\\bin";
        Path binPath = Path.of(binPathString);
        FileUtils.deleteDirectory(binPath.toFile());

        // generating
        FileGenerator generator = new FileGenerator();
        Files.createDirectory(binPath);

        generator.generateIndexFile(generalInfo,binPathString);

        System.out.println("File generate process ended successfully");


        System.out.println("Done! output has been saved to "+binPath);
    }

    public static String readStringResource(String path) throws IOException {
        InputStream stream = Main.class.getResource(path).openStream();
        return new String(stream.readAllBytes());
    }
}