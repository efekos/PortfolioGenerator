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
import dev.efekos.pg.process.ProcessContext;
import dev.efekos.pg.resource.IconResource;
import dev.efekos.pg.resource.Resource;
import dev.efekos.pg.resource.ResourceManager;
import dev.efekos.pg.resource.Resources;
import dev.efekos.pg.util.Locale;
import dev.efekos.pg.util.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

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
            timelineElements.add("<li>" + generateAboutEntry(event.getIcon(), event.getTitle(), event.getTime(), false) + "</li>");
        }

        String fileString = ResourceManager.getResource(Resources.HTML_INDEX_PAGE)
                .replaceAll("%%name%%", info.getName())
                .replaceAll("%%title%%", info.getTitle())
                .replaceAll("%%welcomer%%", info.getWelcomer())
                .replaceAll("%%aboutEntries%%", "<div class=\"entries\">" + String.join("",
                        Arrays.asList(
                                generateAboutEntry("birth", Text.translated("about.age"), "", true),
                                generateAboutEntry("language", Text.translated("about.lang_native"), info.getNativeLanguage().name(), false),
                                generateAboutEntry("language", Text.translated("about.lang_known"), String.join(", ", info.getKnownLanguages().stream().map(Locale::name).toList()), false),
                                generateAboutEntry("university", Text.translated("about.certificates"), context.certificates.size() + "", false),
                                generateAboutEntry("project", Text.translated("about.projects"), context.projects.size() + "", false),
                                generateAboutEntry("briefcase", Text.translated("about.jobs"), context.experienceInfo.getEntries().size() + "", false),
                                generateAboutEntry("letter", Text.translated("about.email"), context.contactInfo.getEmail(), false),
                                generateAboutEntry("phone", Text.translated("about.phone"), context.contactInfo.getNumber(), false)
                        )
                ) + "</div>")
                .replaceAll("%%timeline%%", String.join("", timelineElements))
                .replaceAll("%%socialElements%%", String.join("", socialLinkElements));

        writeFile(binPath + "\\index.html", fileString);

        Main.LOGGER.success("Generated file: index.html");
    }

    public void generateContactPage(GeneralInfo generalInfo, ContactInfo contactInfo) throws IOException {
        Main.LOGGER.info("Generating file: contact.html");

        List<String> socialLinkElements = new ArrayList<>();
        generalInfo.getSocialLinks().forEach((type, link) -> {
            try {
                socialLinkElements.add(generateSocialElement(type, link));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        String file = ResourceManager.getResource(Resources.HTML_CONTACT_PAGE);

        if (contactInfo.isIncludeSocials()) {
            file = file.replaceAll(
                    "%%social%%", """
                            <h2>You can use my social media accounts as well</h2>
                            %%socialElements%%
                            """
            );
        } else file = file.replaceAll("%%social%%", "");

        writeFile(binPath + "\\contact.html", file
                .replaceAll("%%name%%", generalInfo.getName())
                .replaceAll("%%mail%%", contactInfo.getEmail())
                .replaceAll("%%phone%%", contactInfo.getNumber())
                .replaceAll("%%places%%", String.join("\n", contactInfo.getPlaces().stream().map(this::generatePlaceEntry).toList()))
                .replaceAll("%%socialElements%%", String.join("", socialLinkElements)));

        Main.LOGGER.success("Generated file: contact.html");
    }

    private String generatePlaceEntry(Place place) {
        String file = ResourceManager.getResource(Resources.HTML_PLACE_ENTRY_TEMPLATE);

        return file
                .replaceAll("%%plmaps%%", place.getMapsLink())
                .replaceAll("%%plwebsite%%", place.getWebsite())
                .replaceAll("%%pldisplay%%", place.getDisplayName())
                .replaceAll("%%pladdress%%", place.getAddress());
    }

    private String generateAboutEntry(String icon, String title, String alt, boolean age) {
        String templateEntry = ResourceManager.getResource(Resources.HTML_ABOUT_ENTRY_TEMPLATE);

        return templateEntry.replaceAll("%%title%%", title)
                .replaceAll("%%icon%%", icon)
                .replaceAll("%%i%%", age ? "id=\"age\"" : "")
                .replaceAll("%%alt%%", alt);
    }

    private String generateSocialElement(SocialLinkType type, String link) throws IOException {
        String templateElement = ResourceManager.getResource(Resources.HTML_SOCIAL_ICON_TEMPLATE);
        return templateElement.replaceAll("%%link%%", link).replaceAll("%%icon%%", type.getId());
    }

    public void generateExperienceFile(GeneralInfo generalInfo, ExperienceInfo experienceInfo) throws IOException {
        ExperiencePageGenerator generator = new ExperiencePageGenerator(binPath);

        generator.generate(generalInfo, experienceInfo);
    }

    public void generateStyleFiles(GeneralInfo info, TagColorInfo tagColorInfo) throws IOException {
        Main.LOGGER.info("Copying static style files");

        copyResource(Resources.STYLE_MAIN, "\\style\\main_style.css", binPath);
        copyResource(Resources.STYLE_CERTIFICATES, "\\style\\certificates.css", binPath);
        copyResource(Resources.STYLE_EDUCATION, "\\style\\education.css", binPath);
        copyResource(Resources.STYLE_PROJECTS, "\\style\\projects.css", binPath);
        copyResource(Resources.STYLE_GALLERY_MODALS, "\\style\\gallery_modals.css", binPath);

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
        Main.DEBUG_LOGGER.info("Generating file: age_calculator.js");
        String string = ResourceManager.getResource(Resources.SCRIPT_AGE_CALCULATOR).replaceAll("%%byear%%", info.getBirthDate().getYear() + "");
        writeFile(binPath + "\\age_calculator.js", string);
        Main.DEBUG_LOGGER.success("Generated file: age_calculator.js");

        // project_search.js
        copyResource(Resources.SCRIPT_PROJECT_SEARCH, "\\projects_search.js", binPath);
        copyResource(Resources.SCRIPT_EXPAND_ENTRIES, "\\expandable_entries.js", binPath);


        Main.DEBUG_LOGGER.info("Generating file: language_finder.js");
        String language = ResourceManager.getResource(Resources.SCRIPT_LANGUAGE_FINDER).replaceAll("%%UUID%%", UUID.randomUUID().toString());
        writeFile(binPath+"\\language_finder.js",language);
        Main.DEBUG_LOGGER.success("Generated file: language_finder.js");


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
        String bioFile = ResourceManager.getResource(Resources.HTML_BIO_PAGE).replaceAll("%%bio%%", bio);

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

        Resources.all().forEach(resource -> {
            if(resource instanceof IconResource iconResource){
                try {
                    copyIcon(iconResource.getPathName(),iconResource.getOutName().replaceAll("/","\\\\"));
                }catch (Exception e){
                    throw new RuntimeException(e);
                }
            }
        });

        for (SocialLinkType type : generalInfo.getSocialLinks().keySet()) {
            copyIcon("social/" + type.getId(), "social\\" + type.getId());
        }

        Files.copy(Path.of(Main.getMainPath().toString(), "data", "profile.png"), Path.of(binPath, "images", "profile.png"));

        writeFile(binPath + "\\images\\icon\\credit_note.txt", "Every icon that doesn't contain a copyright is from heroicons.com (not including 'social' folder).");


        Main.LOGGER.success("Copied icons");
    }

    private void copyIcon(String resourceName, String binName) throws IOException {
        Main.DEBUG_LOGGER.info("Copying icon: " + resourceName);
        Optional<Resource> optional = Resources.all().stream().filter(resource ->
                        resource instanceof IconResource iconResource ? iconResource.getPathName().equals(resourceName):
                        resource.getPathName().equals("/site/icon/" + resourceName + ".svg")
                ).findFirst();
        if (optional.isEmpty())
            throw new MissingResourceException("Missing icon", "dev.efekos.pg.Main", "/site/icon/" + resourceName + ".svg");

        String string = ResourceManager.getResource(optional.get());
        writeFile(binPath + "\\images\\icon\\" + binName + ".svg", string);
        Main.DEBUG_LOGGER.success("Copied icon: " + resourceName);
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

        copyResource(Resources.SCRIPT_LIBRARY_MARKED, "\\lib\\marked.js", binPath);
        copyResource(Resources.SCRIPT_LIBRARY_PRISM, "\\lib\\prism.js", binPath);
        copyResource(Resources.STYLE_LIBRARY_PRISM, "\\lib\\prism.css", binPath);

        Main.LOGGER.success("Copied library files");
    }

    public void copyLanguageFiles() throws IOException {
        Main.LOGGER.info("Copying language files");
        copyResource(Resources.JSON_LANGUAGE_EN,"\\lang\\en.json",binPath);
        copyResource(Resources.JSON_LANGUAGE_TR,"\\lang\\tr.json",binPath);
        Main.LOGGER.info("Copied language files");
    }
}