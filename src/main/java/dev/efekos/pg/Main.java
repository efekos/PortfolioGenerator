package dev.efekos.pg;

import dev.efekos.pg.data.DataGrabber;
import dev.efekos.pg.data.schema.EducationInfo;
import dev.efekos.pg.data.schema.ExperienceInfo;
import dev.efekos.pg.data.schema.GeneralInfo;
import dev.efekos.pg.output.FileGenerator;
import dev.efekos.pg.util.WorkContext;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    /**
     * Main path that the program is running.
     */
    private static String MAIN_PATH;

    /**
     * Returns a main path.
     * @return main path that the program is running.
     */
    public static Path getMainPath() {
        return Path.of(MAIN_PATH);
    }

    private static final WorkContext context = new WorkContext();

    /**
     * Main method
     * @param args u now at it izz
     * @throws Exception If anything goes wrong
     */
    public static void main(String[] args)throws Exception {
        MAIN_PATH = System.getProperty("user.dir");
        System.out.println("Hello World!");


        startDataGrabProcess(); // read files
        startBinRefreshProcess(); // clear bin folder
        startFileGenerateProcess(); // generate files

        System.out.println("Done! output has been saved to "+context.getBinPath());
    }

    private static void startFileGenerateProcess() throws IOException {
        System.out.println("Starting file generate process");
        FileGenerator generator = new FileGenerator();

        generator.generateIndexFile(context.getGrabbedGeneralInfo(), context.getBinPath());
        System.out.println("File generate process ended successfully");
    }

    private static void startBinRefreshProcess() throws IOException {
        System.out.println("Starting refresh bin process");
        String binPathString = MAIN_PATH+"\\bin";
        Path binPath = Path.of(binPathString);
        FileUtils.deleteDirectory(binPath.toFile());

        Files.createDirectory(binPath);

        context.setBinPath(binPathString);
        System.out.println("Bin refresh process ended successfully");
    }

    private static void startDataGrabProcess() throws IOException {
        System.out.println("Starting data grab process");
        DataGrabber grabber = new DataGrabber(MAIN_PATH);

        GeneralInfo generalInfo = grabber.grabGeneralInfo();
        EducationInfo educationInfo = grabber.grabEducationInfo();
        ExperienceInfo info = grabber.grabExperienceInfO();

        context.setGrabbedGeneralInfo(generalInfo);
        context.setGrabbedEducationInfo(educationInfo);
        context.setExperienceInfo(info);
        System.out.println("Data grab process ended successfully");
    }








    /**
     * Reads a resource from the resources folder, and returns it as a {@link String}.
     * @param path Path to the file you want to reach.
     * @return A file if found as {@link String}
     * @throws IOException If {@link URL#openStream()} method fails.
     */
    public static String readStringResource(String path) throws IOException {
        InputStream stream = Main.class.getResource(path).openStream();
        return new String(stream.readAllBytes());
    }
}