package dev.efekos.pg.output;

import dev.efekos.pg.Main;
import dev.efekos.pg.data.schema.Certificate;
import dev.efekos.pg.data.schema.EducationInfo;
import dev.efekos.pg.data.schema.ExperienceInfo;
import dev.efekos.pg.data.schema.GeneralInfo;
import dev.efekos.pg.util.Locale;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileGenerator implements Generator {

    private final String binPath;

    public FileGenerator(String binPath) {
        this.binPath = binPath;
    }

    public void generateIndexFile(GeneralInfo info) throws IOException {
        System.out.println("Generating file: index.html");

        String fileString = Main.readStringResource("/site/index.html")
                .replaceAll("%%name%%", info.getName())
                .replaceAll("%%title%%", info.getTitle())
                .replaceAll("%%welcomer%%", info.getWelcomer())
                .replaceAll("%%native%%", info.getNativeLanguage().name())
                .replaceAll("%%known%%",String.join(", ",info.getKnownLanguages().stream().map(Locale::name).toList()));

        writeFile(binPath + "\\index.html", fileString);

        System.out.println("Generated file: index.html");
    }

    public void generateExperienceFile(GeneralInfo generalInfo, ExperienceInfo experienceInfo)throws IOException{
        ExperiencePageGenerator generator = new ExperiencePageGenerator(binPath);

        generator.generate(generalInfo,experienceInfo);
    }

    public void copyStyleFiles() throws IOException {
        System.out.println("Copying style files");

        copyStringResource("/site/style.css", "\\style\\main_style.css", binPath);
        copyStringResource("/site/style_certificates.css", "\\style\\certificates.css", binPath);
        copyStringResource("/site/style_education.css", "\\style\\education.css", binPath);

        System.out.println("Copied all style files");
    }

    public void generateScriptFiles(GeneralInfo info) throws IOException {
        System.out.println("Generating script files");

        String string = Main.readStringResource("/site/age_calculator.js").replaceAll("%%byear%%",info.getBirthDate().getYear()+"");
        writeFile(binPath+"\\age_calculator.js",string);

        System.out.println("Generates script files");
    }

    public void generateCertificatesFile(GeneralInfo info, List<Certificate> certificates) throws IOException {
        System.out.println("Generating certificate pages");

        CertificatesPageGenerator generator = new CertificatesPageGenerator(binPath);

        generator.generateActualFile(info, certificates);
        generator.copyImages(info, certificates);
        generator.generateSinglePages(info, certificates);

        System.out.println("Generated certificate pages");
    }

    public void generateBioFile(GeneralInfo info) throws IOException {
        System.out.println("Generating file: bio.html");


        String bio = info.getBio();
        String bioFile = Main.readStringResource("/site/bio.html").replaceAll("%%bio%%", bio);

        writeFile(binPath + "\\bio.html", bioFile);


        System.out.println("Generated file: bio.html");
    }

    public void generateEducationFile(GeneralInfo info, EducationInfo educationInfo)throws IOException{
        EducationPageGenerator generator = new EducationPageGenerator(binPath);
        generator.generate(info,educationInfo);
    }

    public void copyIcons() throws IOException {
        System.out.println("Copying icons");

        Files.createDirectory(Path.of(binPath, "images", "icon"));

        copyIcon("external_site", "external");
        copyIcon("clock","clock");
        copyIcon("location","location");
        copyIcon("university","university");
        copyIcon("briefcase","briefcase");

        Files.copy(Path.of(Main.getMainPath().toString(),"data","profile.png"),Path.of(binPath,"images","profile.png"));

        writeFile(binPath+"\\images\\icon\\credit_note.txt","Every icon used here is from heroicons.com");


        System.out.println("Copied icons");
    }

    private void copyIcon(String resourceName, String binName) throws IOException {
        System.out.println("Copying icon: " + resourceName);
        String string = Main.readStringResource("/site/icon/" + resourceName + ".svg");
        writeFile(binPath + "\\images\\icon\\" + binName + ".svg", string);
        System.out.println("Copied icon: " + resourceName);
    }
}
