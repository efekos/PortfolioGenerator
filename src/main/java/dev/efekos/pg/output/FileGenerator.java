package dev.efekos.pg.output;

import dev.efekos.pg.Main;
import dev.efekos.pg.data.schema.Certificate;
import dev.efekos.pg.data.schema.GeneralInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public class FileGenerator {

    private final String binPath;

    /**
     * Constructs a new {@link FileGenerator}
     *
     * @param binPath Path to the output folder.
     */
    public FileGenerator(String binPath) {
        this.binPath = binPath;
    }

    /**
     * Generates a main <code>index.html</code> file, the home page for the entire website.
     *
     * @param info Grabbed {@link GeneralInfo} to place its data to the page.
     * @throws IOException If something goes wrong
     */
    public void generateIndexFile(GeneralInfo info) throws IOException {
        System.out.println("Generating file: index.html");

        String fileString = Main.readStringResource("/site/index.html")
                .replaceAll("%%name%%", info.getName())
                .replaceAll("%%title%%", info.getTitle())
                .replaceAll("%%welcomer%%", info.getWelcomer());

        writeFile(binPath + "\\index.html", fileString);

        System.out.println("Generated file: index.html");
    }

    public void copyStyleFiles()throws IOException{
        System.out.println("Copying style files");

        copyResource("/site/style.css","\\style\\main_style.css");
        copyResource("/site/style_certificates.css","\\style\\certificates.css");

        System.out.println("Copied all style files");
    }

    private void writeFile(String path, String content) throws IOException {
        File file = new File(path);

        file.createNewFile();

        FileWriter writer = new FileWriter(file);

        writer.write(content);
        writer.flush();
        writer.close();
    }

    private String makeId(String id){
        return id
                .replaceAll(" ","_")
                .toLowerCase(Locale.ROOT);
    }

    public void generateCertificatesFile(GeneralInfo info, List<Certificate> certificates) throws IOException {
        System.out.println("Generating file: certificates.html");

        List<String> elementList = certificates.stream().map(certificate -> "<a href=\"./certificate/"+makeId(certificate.getDisplay().getTitle())+".html\"><img src=\"./images/certificate/" + certificate.getDisplay().getImage() + "\", alt=\"" + certificate.getDisplay().getTitle() + "\"></img>").toList();

        String fileString = Main.readStringResource("/site/certificates.html")
                .replaceAll("%%name%%", info.getName())
                .replaceAll("%%title%%", info.getTitle())
                .replaceAll("%%images%%", String.join("", elementList));

        writeFile(binPath + "\\certificates.html", fileString);
        System.out.println("Generated file: certificates.html");

        System.out.println("Copying images for file: certificates.html");

        for (Certificate certificate : certificates) {
            for (String value : certificate.getImages().values()) {
                Path valuePath = Path.of(value); // data daki dosya
                if (!Files.exists(valuePath)) throw new FileNotFoundException(value);

                String imagePath = value.replace(Main.getMainPath() + "\\data\\certificates", "");
                String certificatesFolderPath = binPath + "\\images\\certificate";
                Files.createDirectories(Path.of(certificatesFolderPath, imagePath).getParent());

                Files.copy(valuePath, Path.of(certificatesFolderPath, imagePath));
            }
        }
    }

    public void copyResource(String resourceLocation, String outputLocation) throws IOException {
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
}
