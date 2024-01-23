package dev.efekos.pg;

import dev.efekos.pg.data.DataGrabber;
import dev.efekos.pg.data.schema.*;
import dev.efekos.pg.output.FileGenerator;
import dev.efekos.pg.util.LocaleHelper;
import dev.efekos.pg.util.WorkContext;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

public class Main {
    /**
     * Main path that the program is running.
     */
    private static String MAIN_PATH;

    /**
     * Returns a main path.
     *
     * @return main path that the program is running.
     */
    public static Path getMainPath() {
        return Path.of(MAIN_PATH);
    }

    private static final WorkContext context = new WorkContext();

    public static void main(String[] args) throws Exception {
        LocaleHelper.loadLocales();
        MAIN_PATH = System.getProperty("user.dir");
        System.out.println("Hello World!");

        long time = new Date().getTime();

        startDataGrabProcess(); // read files
        startBinRefreshProcess(); // clear bin folder
        startFileGenerateProcess(); // generate files

        long time2 = new Date().getTime();

        float seconds = (float) (time2 - time) / 1000;

        System.out.println("Done in " + seconds + "s! output has been saved to " + context.binPath);
    }

    private static void startFileGenerateProcess() throws Exception {
        System.out.println("Starting file generate process");
        long time = new Date().getTime();
        FileGenerator generator = new FileGenerator(context.binPath);

        // generating
        generator.generateIndexFile(context.generalInfo);
        generator.generateCertificatesFile(context.generalInfo, context.certificates);
        generator.generateBioFile(context.generalInfo);
        generator.generateScriptFiles(context.generalInfo);
        generator.generateEducationFile(context.generalInfo, context.educationInfo);
        generator.generateExperienceFile(context.generalInfo, context.experienceInfo);
        generator.generateStyleFiles(context.generalInfo,context.tagColorInfo);
        generator.generateProjectsPage(context.generalInfo, context.projects);
        generator.copyLibraries();

        // copying
        generator.copyIcons(context.generalInfo);


        long time2 = new Date().getTime();
        float difference = (float) (time2 - time) / 1000;
        System.out.println("File generate process ended successfully in " + difference + "s");
        System.out.println("------------");
    }

    private static void startBinRefreshProcess() throws IOException {
        System.out.println("Starting refresh bin process");
        long time = new Date().getTime();

        String binPathString = MAIN_PATH + "\\bin";
        Path binPath = Path.of(binPathString);
        FileUtils.deleteDirectory(binPath.toFile());

        Files.createDirectory(binPath);

        context.binPath = binPathString;

        long time2 = new Date().getTime();
        float difference = (float) (time2 - time) / 1000;
        System.out.println("Bin refresh process ended successfully in " + difference + "s");
        System.out.println("------------");
    }

    private static void startDataGrabProcess() throws IOException {
        System.out.println("Starting data grab process");
        long time = new Date().getTime();

        DataGrabber grabber = new DataGrabber(MAIN_PATH);

        GeneralInfo generalInfo = grabber.grabGeneralInfo();
        EducationInfo educationInfo = grabber.grabEducationInfo();
        ExperienceInfo info = grabber.grabExperienceInfo();
        List<Certificate> certificates = grabber.grabCertificates();
        List<Project> projects = grabber.grabProjects();
        TagColorInfo grabTagColorInfo = grabber.grabTagColorInfo();

        context.generalInfo = generalInfo;
        context.educationInfo = educationInfo;
        context.experienceInfo = info;
        context.certificates = certificates;
        context.projects = projects;
        context.tagColorInfo = grabTagColorInfo;

        long time2 = new Date().getTime();
        float difference = (float) (time2 - time) / 1000;
        System.out.println("Data grab process ended successfully in " + difference + "s");
        System.out.println("------------");
    }


    /**
     * Reads a resource from the resources folder, and returns it as a {@link String}.
     *
     * @param path Path to the file you want to reach.
     * @return A file if found as {@link String}
     * @throws IOException If {@link URL#openStream()} method fails.
     */
    public static String readStringResource(String path) throws IOException {
        InputStream stream = Main.class.getResource(path).openStream();
        return new String(stream.readAllBytes());
    }
}