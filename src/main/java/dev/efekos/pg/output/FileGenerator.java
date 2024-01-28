/*
 * Copyright 2024 efekos
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the “Software”), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package dev.efekos.pg.output;

import dev.efekos.pg.Main;
import dev.efekos.pg.data.schema.*;
import dev.efekos.pg.data.timeline.TimelineEvent;
import dev.efekos.pg.data.type.SocialLinkType;
import dev.efekos.pg.util.Locale;
import dev.efekos.pg.process.ProcessContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FileGenerator implements Generator {

    private final String binPath;

    public FileGenerator(String binPath) {
        this.binPath = binPath;
    }

    public void generateIndexFile(ProcessContext context) throws IOException {
        GeneralInfo info = context.generalInfo;
        Main.LOGGER.info("Generating file: index.html");

        List<String> socialLinkElements = new ArrayList<>();
        info.getSocialLinks().forEach((type, link) -> {
            try {
                socialLinkElements.add(generateSocialElement(type, link));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        List<String> timelineElements = new ArrayList<>();
        List<TimelineEvent> timeline = context.collectedTimeline;
        timeline.sort(Comparator.comparing(TimelineEvent::getDate));

        for (TimelineEvent event : timeline) {
            timelineElements.add("<li>"+generateAboutEntry(event.getIcon(), event.getTitle(), event.getTime(), false)+"</li>");
        }

        String fileString = Main.readStringResource("/site/html/index.html")
                .replaceAll("%%name%%", info.getName())
                .replaceAll("%%title%%", info.getTitle())
                .replaceAll("%%welcomer%%", info.getWelcomer())
                .replaceAll("%%aboutEntries%%", "<div class=\"entries\">" + String.join("",
                        Arrays.asList(
                                generateAboutEntry("birth", "Age", "", true),
                                generateAboutEntry("language", "Native Language", info.getNativeLanguage().name(), false),
                                generateAboutEntry("language", "Known Languages", String.join(", ", info.getKnownLanguages().stream().map(Locale::name).toList()), false),
                                generateAboutEntry("university","Certificates Earned",context.certificates.size()+"",false),
                                generateAboutEntry("project","Projects Made",context.projects.size()+"",false),
                                generateAboutEntry("briefcase","Jobs Worked",context.experienceInfo.getEntries().size()+"",false),
                                generateAboutEntry("letter","Email Address",context.contactInfo.getEmail(),false),
                                generateAboutEntry("phone","Phone Number",context.contactInfo.getNumber(),false)
                        )
                ) + "</div>")
                .replaceAll("%%timeline%%",String.join("",timelineElements))
                .replaceAll("%%socialElements%%", String.join("", socialLinkElements));

        writeFile(binPath + "\\index.html", fileString);

        Main.LOGGER.success("Generated file: index.html");
    }

    public void generateContactPage(GeneralInfo generalInfo,ContactInfo contactInfo) throws IOException{
        Main.LOGGER.info("Generating file: contact.html");

        List<String> socialLinkElements = new ArrayList<>();
        generalInfo.getSocialLinks().forEach((type, link) -> {
            try {
                socialLinkElements.add(generateSocialElement(type, link));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        String file = Main.readStringResource("/site/html/contact.html");

        if(contactInfo.isIncludeSocials()){
            file = file.replaceAll(
                    "%%social%%", """
                            <h2>You can use my social media accounts as well</h2>
                            %%socialElements%%
                            """
            );
        } else file = file.replaceAll("%%social%%","");

        writeFile(binPath+"\\contact.html",file
                .replaceAll("%%name%%",generalInfo.getName())
                .replaceAll("%%mail%%",contactInfo.getEmail())
                .replaceAll("%%phone%%",contactInfo.getNumber())
                .replaceAll("%%places%%",String.join("\n",contactInfo.getPlaces().stream().map(this::generatePlaceEntry).toList()))
                .replaceAll("%%socialElements%%",String.join("",socialLinkElements)));

        Main.LOGGER.success("Generated file: contact.html");
    }

    private String generatePlaceEntry(Place place){
        String file = Main.readStringResource("/site/html/template/place_entry.html");

        return file
                .replaceAll("%%plmaps%%",place.getMapsLink())
                .replaceAll("%%plwebsite%%",place.getWebsite())
                .replaceAll("%%pldisplay%%",place.getDisplayName())
                .replaceAll("%%pladdress%%",place.getAddress());
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
        Main.LOGGER.info("Copying static style files");

        copyStringResource("/site/style/style.css", "\\style\\main_style.css", binPath);
        copyStringResource("/site/style/style_certificates.css", "\\style\\certificates.css", binPath);
        copyStringResource("/site/style/style_education.css", "\\style\\education.css", binPath);
        copyStringResource("/site/style/style_projects.css", "\\style\\projects.css", binPath);
        copyStringResource("/site/style/style_gallery_modals.css", "\\style\\gallery_modals.css", binPath);

        Main.LOGGER.success("Copied all static style files");

        Main.LOGGER.info("Generating non-static style files");

        StyleFileGenerator generator = new StyleFileGenerator(binPath);

        generator.generateProjectTags(tagColorInfo);
        generator.generateSocialIcons(info);
        generator.generateProjectVersions();

        Main.LOGGER.success("Generates all non-static style files");
    }

    public void generateScriptFiles(GeneralInfo info) throws IOException {
        Main.LOGGER.info("Generating script files");

        // age_calculator.js
        String string = Main.readStringResource("/site/script/age_calculator.js").replaceAll("%%byear%%", info.getBirthDate().getYear() + "");
        writeFile(binPath + "\\age_calculator.js", string);

        // project_search.js
        copyStringResource("/site/script/projects_search.js","\\projects_search.js",binPath);
        copyStringResource("/site/script/expandable_entries.js","\\expandable_entries.js",binPath);

        Main.LOGGER.success("Generates script files");
    }

    public void generateCertificatesFile(GeneralInfo info, List<Certificate> certificates) throws IOException {
        Main.LOGGER.info("Generating certificate pages");

        CertificatesPageGenerator generator = new CertificatesPageGenerator(binPath);

        generator.generateActualFile(info, certificates);
        generator.copyImages(certificates);
        generator.generateSinglePages(info, certificates);

        Main.LOGGER.success("Generated certificate pages");
    }

    public void generateBioFile(GeneralInfo info) throws IOException {
        Main.LOGGER.info("Generating file: bio.html");


        String bio = info.getBio();
        String bioFile = Main.readStringResource("/site/html/bio.html").replaceAll("%%bio%%", bio);

        writeFile(binPath + "\\bio.html", bioFile);


        Main.LOGGER.success("Generated file: bio.html");
    }

    public void generateEducationFile(GeneralInfo info, EducationInfo educationInfo) throws IOException {
        EducationPageGenerator generator = new EducationPageGenerator(binPath);
        generator.generate(info, educationInfo);
    }

    public void copyIcons(GeneralInfo generalInfo) throws IOException {
        Main.LOGGER.info("Copying icons");

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
        copyIcon("project","project");
        copyIcon("phone","phone");
        copyIcon("letter","letter");
        copyIcon("expanded","expanded");
        copyIcon("unexpanded","unexpanded");
        copyIcon("school","school");
        copyIcon("top","top");

        copyIcon("project_link/doc","link\\doc");
        copyIcon("project_link/guide","link\\guide");
        copyIcon("project_link/issues","link\\issues");
        copyIcon("project_link/main","link\\main");
        copyIcon("project_link/source","link\\source");
        copyIcon("project_link/support","link\\support");
        copyIcon("project_link/wiki","link\\wiki");

        for (SocialLinkType type : generalInfo.getSocialLinks().keySet()) {
            copyIcon("social/" + type.getId(), "social\\" + type.getId());
        }

        Files.copy(Path.of(Main.getMainPath().toString(), "data", "profile.png"), Path.of(binPath, "images", "profile.png"));

        writeFile(binPath + "\\images\\icon\\credit_note.txt", "Every icon used here is from heroicons.com");


        Main.LOGGER.success("Copied icons");
    }

    private void copyIcon(String resourceName, String binName) throws IOException {
        Main.LOGGER.info("Copying icon: " + resourceName);
        String string = Main.readStringResource("/site/icon/" + resourceName + ".svg");
        writeFile(binPath + "\\images\\icon\\" + binName + ".svg", string);
        Main.LOGGER.success("Copied icon: " + resourceName);
    }

    public void generateProjectsPage(GeneralInfo generalInfo, List<Project> projects) throws Exception {
        Main.LOGGER.info("Generating projects page");
        ProjectPageGenerator generator = new ProjectPageGenerator(binPath);

        generator.generateMainPage(generalInfo, projects);
        generator.generateSinglePages(generalInfo, projects);
        Main.LOGGER.success("Generated projects page");
    }

    public void copyLibraries() throws IOException {
        Main.LOGGER.info("Copying library files");

        copyStringResource("/site/lib/marked.js", "\\lib\\marked.js", binPath);
        copyStringResource("/site/lib/prism.js", "\\lib\\prism.js", binPath);
        copyStringResource("/site/lib/prism.css", "\\lib\\prism.css", binPath);

        Main.LOGGER.success("Copied library files");
    }
}