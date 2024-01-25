package dev.efekos.pg.output;

import dev.efekos.pg.Main;
import dev.efekos.pg.data.schema.*;
import dev.efekos.pg.data.type.SocialLinkType;
import dev.efekos.pg.util.Locale;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileGenerator implements Generator {

    private final String binPath;

    public FileGenerator(String binPath) {
        this.binPath = binPath;
    }

    public void generateIndexFile(GeneralInfo info) throws IOException {
        System.out.println("Generating file: index.html");

        List<String> socialLinkElements = new ArrayList<>();
        info.getSocialLinks().forEach((type, link) -> {
            try {
                socialLinkElements.add(generateSocialElement(type, link));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        String fileString = Main.readStringResource("/site/index.html")
                .replaceAll("%%name%%", info.getName())
                .replaceAll("%%title%%", info.getTitle())
                .replaceAll("%%welcomer%%", info.getWelcomer())
                .replaceAll("%%aboutEntries%%", "<div class=\"entries\">" + String.join("",
                        Arrays.asList(
                                generateAboutEntry("birth", "Age", "", true),
                                generateAboutEntry("language", "Native Language", info.getNativeLanguage().name(), false),
                                generateAboutEntry("language", "Known Languages", String.join(", ", info.getKnownLanguages().stream().map(Locale::name).toList()), false)
                        )
                ) + "</div>")
                .replaceAll("%%socialElements%%", String.join("", socialLinkElements));

        writeFile(binPath + "\\index.html", fileString);

        System.out.println("Generated file: index.html");
    }

    private String generateAboutEntry(String icon, String title, String alt, boolean age){
        String templateEntry = Main.readStringResource("/site/html/template/about_entry.html");

        return templateEntry.replaceAll("%%title%%", title)
                .replaceAll("%%icon%%", icon)
                .replaceAll("%%i%%", age ? "id=\"age\"" : "")
                .replaceAll("%%alt%%", alt);
    }

    private String generateSocialElement(SocialLinkType type, String link) throws IOException{
        String templateElement = Main.readStringResource("/site/html/template/social_icon.html");
        return templateElement.replaceAll("%%link%%", link).replaceAll("%%icon%%", type.getId());
    }

    public void generateExperienceFile(GeneralInfo generalInfo, ExperienceInfo experienceInfo) throws IOException {
        ExperiencePageGenerator generator = new ExperiencePageGenerator(binPath);

        generator.generate(generalInfo, experienceInfo);
    }

    public void generateStyleFiles(GeneralInfo info,TagColorInfo tagColorInfo) throws IOException {
        System.out.println("Copying static style files");

        copyStringResource("/site/style/style.css", "\\style\\main_style.css", binPath);
        copyStringResource("/site/style/style_certificates.css", "\\style\\certificates.css", binPath);
        copyStringResource("/site/style/style_education.css", "\\style\\education.css", binPath);
        copyStringResource("/site/style/style_projects.css", "\\style\\projects.css", binPath);
        copyStringResource("/site/style/style_gallery_modals.css", "\\style\\gallery_modals.css", binPath);

        System.out.println("Copied all static style files");

        System.out.println("Generating non-static style files");

        StyleFileGenerator generator = new StyleFileGenerator(binPath);

        generator.generateProjectTags(tagColorInfo);
        generator.generateSocialIcons(info);
        generator.generateProjectVersions();

        System.out.println("Generates all non-static style files");
    }

    public void generateScriptFiles(GeneralInfo info) throws IOException {
        System.out.println("Generating script files");

        String string = Main.readStringResource("/site/script/age_calculator.js").replaceAll("%%byear%%", info.getBirthDate().getYear() + "");
        writeFile(binPath + "\\age_calculator.js", string);

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
        String bioFile = Main.readStringResource("/site/html/bio.html").replaceAll("%%bio%%", bio);

        writeFile(binPath + "\\bio.html", bioFile);


        System.out.println("Generated file: bio.html");
    }

    public void generateEducationFile(GeneralInfo info, EducationInfo educationInfo) throws IOException {
        EducationPageGenerator generator = new EducationPageGenerator(binPath);
        generator.generate(info, educationInfo);
    }

    public void copyIcons(GeneralInfo generalInfo) throws IOException {
        System.out.println("Copying icons");

        Files.createDirectory(Path.of(binPath, "images", "icon"));

        copyIcon("external_site", "external");
        copyIcon("clock", "clock");
        copyIcon("location", "location");
        copyIcon("university", "university");
        copyIcon("briefcase", "briefcase");
        copyIcon("language", "language");
        copyIcon("birth", "birth");
        copyIcon("scale", "scale");
        copyIcon("tag", "tag");
        for (SocialLinkType type : generalInfo.getSocialLinks().keySet()) {
            copyIcon("social/" + type.getId(), "social\\" + type.getId());
        }

        Files.copy(Path.of(Main.getMainPath().toString(), "data", "profile.png"), Path.of(binPath, "images", "profile.png"));

        writeFile(binPath + "\\images\\icon\\credit_note.txt", "Every icon used here is from heroicons.com");


        System.out.println("Copied icons");
    }

    private void copyIcon(String resourceName, String binName) throws IOException {
        System.out.println("Copying icon: " + resourceName);
        String string = Main.readStringResource("/site/icon/" + resourceName + ".svg");
        writeFile(binPath + "\\images\\icon\\" + binName + ".svg", string);
        System.out.println("Copied icon: " + resourceName);
    }

    public void generateProjectsPage(GeneralInfo generalInfo, List<Project> projects) throws Exception {
        System.out.println("Generating projects page");
        ProjectPageGenerator generator = new ProjectPageGenerator(binPath);

        generator.generateMainPage(generalInfo, projects);
        generator.generateSinglePages(generalInfo, projects);
        System.out.println("Generated projects page");
    }

    public void copyLibraries() throws IOException {
        System.out.println("Copying library files");

        copyStringResource("/site/lib/marked.js", "\\lib\\marked.js", binPath);
        copyStringResource("/site/lib/prism.js", "\\lib\\prism.js", binPath);
        copyStringResource("/site/lib/prism.css", "\\lib\\prism.css", binPath);

        System.out.println("Copied library files");
    }
}