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
import java.util.Map;

public class FileGenerator implements Generator{

    private final String binPath;

    public FileGenerator(String binPath) {
        this.binPath = binPath;
    }

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

        copyResource("/site/style.css","\\style\\main_style.css",binPath);
        copyResource("/site/style_certificates.css","\\style\\certificates.css",binPath);

        System.out.println("Copied all style files");
    }

    public void generateCertificatesFile(GeneralInfo info, List<Certificate> certificates) throws IOException {
        System.out.println("Generating certificate pages");

        CertificatesPageGenerator generator = new CertificatesPageGenerator(binPath);

        generator.generateActualFile(info, certificates);
        generator.copyImages(info,certificates);
        generator.generateSinglePages(info,certificates);
    }

    public void generateBioFile(GeneralInfo info) throws IOException{
        System.out.println("Generating file: bio.html");


        String bio = info.getBio();
        String bioFile = Main.readStringResource("/site/bio.html").replaceAll("%%bio%%",bio);

        writeFile(binPath+"\\bio.html",bioFile);


        System.out.println("Generated file: bio.html");
    }

    public void copyIcons() throws IOException{
        System.out.println("Copying icons");

        Files.createDirectory(Path.of(binPath,"images","icon"));

        copyIcon("external_site","external");

        System.out.println("Copied icons");
    }

    private void copyIcon(String resourceName,String binName) throws IOException {
        System.out.println("Copying icon: "+resourceName);
        String string = Main.readStringResource("/site/icon/"+resourceName+".svg");
        writeFile(binPath+"\\images\\icon\\"+binName+".svg",string);
        System.out.println("Copied icon: "+resourceName);
    }
}
