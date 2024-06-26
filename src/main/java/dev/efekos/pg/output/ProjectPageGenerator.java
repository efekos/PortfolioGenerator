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
import dev.efekos.pg.data.schema.GeneralInfo;
import dev.efekos.pg.data.schema.Project;
import dev.efekos.pg.data.schema.ProjectGalleryImage;
import dev.efekos.pg.data.type.ProjectLinkType;
import dev.efekos.pg.resource.ResourceManager;
import dev.efekos.pg.resource.Resources;
import dev.efekos.pg.util.Text;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProjectPageGenerator implements Generator {
    private final String binPath;

    private final String footer;

    public ProjectPageGenerator(String binPath, String footer) {
        this.binPath = binPath;
        this.footer = footer;
    }

    private void generateScripts(Project project) throws IOException {
        Main.LOGGER.info("Generating scripts for project: ", project.getId());
        generateReadmeFinder(project);
        generateGalleryModals(project);
        Main.LOGGER.success("Generated scripts for project: ", project.getId());
    }

    private void generateGalleryModals(Project project) throws IOException {
        Main.DEBUG_LOGGER.info("Generating file: projects/", project.getId() + "/gallery_modals.js");
        List<String> codeblocks = new ArrayList<>();
        String template = ResourceManager.getResource(Resources.SCRIPT_GALLERY_MODAL_TEMPLATE);

        project.getGalleryImages().forEach(image -> {
            String generated = template.replaceAll("%%imid%%", image.getId());
            codeblocks.add(generated);
        });

        writeFile(binPath + "\\projects\\" + project.getId() + "\\gallery_modals.js", String.join("\n\n", codeblocks), footer);
        Main.DEBUG_LOGGER.success("Generated file: projects/", project.getId() + "/gallery_modals.js");
    }

    private void generateReadmeFinder(Project project) throws IOException {
        Main.DEBUG_LOGGER.info("Generating file: projects/" + project.getId() + "/readme_finder.js");
        String readmeFinder = ResourceManager.getResource(Resources.SCRIPT_README_FINDER)
                .replaceAll("%%link%%", project.getReadmeFile());
        writeFile(binPath + "\\projects\\" + project.getId() + "\\readme_finder.js", readmeFinder, footer);

        String file = ResourceManager.getResource(Resources.SCRIPT_CHANGELOG_FINDER)
                .replaceAll("%%link%%", project.getChangeLogFile());
        writeFile(binPath + "\\projects\\" + project.getId() + "\\changelog_finder.js", file, footer);

        Main.DEBUG_LOGGER.success("Generated file: projects/" + project.getId() + "/readme_finder.js");
    }

    public void generateSinglePage(GeneralInfo info, Project project) throws IOException {
        Path mainDirectory = Path.of(binPath, "projects", project.getId());
        Path mainDataDirectory = Path.of(Main.getMainPath().toString(), "data", "projects", project.getId());

        mainDirectory.toFile().mkdirs();
        Main.LOGGER.info("Generating directory: projects/", project.getId());

        String tags = String.join("", project.getTags().stream().map(s -> "<div class=\"project-tag-" + s + "\">" + s + "</div>").toList());
        String links = String.join("<br>", generateLinkElements(project.getLinks()));

        //index.html
        Main.DEBUG_LOGGER.info("Generating file: projects/", project.getId(), "/index.html");
        String html = ResourceManager.getResource(Resources.HTML_PROJECT_PAGE)
                .replace("%%name%%", info.getName())
                .replaceAll("%%prname%%", project.getDisplayName())
                .replaceAll("%%tags%%", tags)
                .replaceAll("%%prmainwebsite%%", project.getMainWebsite())
                .replaceAll("%%links%%", links)
                .replaceAll("%%prsummary%%", project.getSummary())
                .replaceAll("%%prid%%", project.getId());

        writeFile(mainDirectory + "\\index.html", html, footer);
        Main.DEBUG_LOGGER.success("Generated file: projects/", project.getId(), "/index.html");

        //license.html
        Main.DEBUG_LOGGER.info("Generating file: projects/", project.getId(), "/license.html");
        String license = ResourceManager.getResource(Resources.HTML_PROJECT_LICENSE_PAGE)
                .replaceAll("%%name%%", info.getName())
                .replaceAll("%%prname%%", project.getDisplayName())
                .replaceAll("%%prid%%", project.getId())
                .replaceAll("%%prlicense%%", Text.translated("title.project.license", project.getLicense()))
                .replaceAll("%%tags%%", tags)
                .replaceAll("%%prsummary%%", project.getSummary())
                .replaceAll("%%links%%", links)
                .replaceAll("%%prmainwebsite%%", project.getMainWebsite())
                .replaceAll("%%prflicense%%", project.getFullLicense());

        writeFile(mainDirectory + "\\license.html", license, footer);
        Main.DEBUG_LOGGER.success("Generated file: projects/", project.getId(), "/license.html");

        //changelog.html
        Main.DEBUG_LOGGER.info("Generating file: projects/", project.getId(), "/changelog.html");
        String changelog = ResourceManager.getResource(Resources.HTML_PROJECT_CHANGELOG_PAGE)
                .replaceAll("%%name%%", info.getName())
                .replaceAll("%%prname%%", project.getDisplayName())
                .replaceAll("%%tags%%", tags)
                .replaceAll("%%links%%", links)
                .replaceAll("%%prmainwebsite%%", project.getMainWebsite())
                .replaceAll("%%prsummary%%", project.getSummary())
                .replaceAll("%%prid%%", project.getId());

        writeFile(mainDirectory + "\\changelog.html", changelog, footer);
        Main.DEBUG_LOGGER.success("Generated file: projects/", project.getId(), "/changelog.html");

        //gallery.html
        Main.DEBUG_LOGGER.info("Generating file: projects/", project.getId(), "/gallery.html");
        String gallery = ResourceManager.getResource(Resources.HTML_PROJECT_GALLERY_PAGE)
                .replaceAll("%%name%%", info.getName())
                .replaceAll("%%prname%%", project.getDisplayName())
                .replaceAll("%%tags%%", tags)
                .replaceAll("%%prsummary%%", project.getSummary())
                .replaceAll("%%links%%", links)
                .replaceAll("%%prmainwebsite%%", project.getMainWebsite())
                .replaceAll("%%prid%%", project.getId())
                .replaceAll("%%images%%", generateGalleryImageElements(project));

        writeFile(mainDirectory + "\\gallery.html", gallery, footer);
        Main.DEBUG_LOGGER.success("Generated file: projects/", project.getId(), "/gallery.html");

        //versions.html
        Main.DEBUG_LOGGER.info("Generating file: projects/", project.getId(), "/versions.html");
        writeFile(mainDirectory + "\\versions.html", new ProjectVersionPageGenerator(info, project, tags, binPath, links, footer).generate(), footer);
        Main.DEBUG_LOGGER.success("Generated file: projects/", project.getId(), "/versions.html");

        //assets
        Path assetsDirectory = Path.of(mainDataDirectory.toString(), "assets");
        FileUtils.copyDirectory(assetsDirectory.toFile(), Path.of(mainDirectory.toString(), "assets").toFile());

        generateScripts(project);
        Main.LOGGER.success("Generated directory: projects/", project.getId());
    }

    private List<String> generateLinkElements(Map<ProjectLinkType, String> links) {
        List<String> elementsGenerated = new ArrayList<>();

        links.forEach((projectLinkType, s) -> elementsGenerated.add("<a target=\"_blank\" href=\"%%link%%\" class=\"project-link\" ><img src=\"../../images/icon/link/%%id%%.svg\" alt=\"%%key%% Icon\" width=\"24\" class=\"project-link-icon\">%%display%%</a>"
                .replaceAll("%%link%%", s)
                .replaceAll("%%display%%", projectLinkType.getDisplay())
                .replaceAll("%%id%%", projectLinkType.getId())
                .replaceAll("%%key%%", StringEscapeUtils.escapeHtml4(Text.translated(projectLinkType.getKey())))
        ));

        return elementsGenerated;
    }

    private String generateGalleryImageElements(Project project) {
        Main.DEBUG_LOGGER.info("Generating gallery image elements for project: ", project.getId());
        List<ProjectGalleryImage> images = project.getGalleryImages().stream().toList();
        String template = ResourceManager.getResource(Resources.HTML_PROJECT_GALLERY_IMAGE_TEMPLATE);

        List<String> generatedElements = new ArrayList<>();

        for (ProjectGalleryImage image : images) {
            String generated = template
                    .replaceAll("%%impath%%", image.getFile())
                    .replaceAll("%%imid%%", image.getId())
                    .replaceAll("%%imname%%", image.getName())
                    .replaceAll("%%imdescription%%", image.getDescription());

            generatedElements.add(generated);
        }

        Main.DEBUG_LOGGER.success("Generated gallery image elements for project: ", project.getId());
        return String.join("\n\n", generatedElements);
    }

    public void generateSinglePages(GeneralInfo info, List<Project> projects) throws IOException {
        Main.LOGGER.info("Generating directory: projects");
        for (Project project : projects) {
            generateSinglePage(info, project);
        }
        Main.LOGGER.info("Generated directory: projects");
    }

    private void copyIcons(List<Project> projects) throws IOException {
        Main.DEBUG_LOGGER.info("Copying project icons");

        for (Project project : projects) {
            copyIcon(project);
        }

        Main.DEBUG_LOGGER.success("Copied project icons");
    }

    public void generateMainPage(GeneralInfo generalInfo, List<Project> projects) throws IOException {
        Main.LOGGER.info("Generating file: projects.html");
        List<String> elements = new ArrayList<>();

        String elementTemplate = ResourceManager.getResource(Resources.HTML_PROJECT_ENTRY_TEMPLATE);
        for (Project project : projects) {
            String element = elementTemplate
                    .replaceAll("%%prname%%", project.getDisplayName())
                    .replaceAll("%%prid%%", project.getId())
                    .replaceAll("%%prversion%%", project.getVersion())
                    .replaceAll("%%prsummary%%", project.getSummary())
                    .replaceAll("%%prlicense%%", project.getLicense())
                    .replaceAll("%%name%%", generalInfo.getName())
                    .replaceAll("%%tags%%", String.join("", project.getTags().stream().map(s -> "<div class=\"project-tag-" + s + "\">" + s + "</div>").toList()))
                    .replaceAll("%%prcreatedate%%", project.getRelease().toString());
            elements.add(element);
        }

        String file = ResourceManager.getResource(Resources.HTML_PROJECTS_PAGE)
                .replaceAll("%%name%%", generalInfo.getName())
                .replaceAll("%%elements%%", String.join("", elements));

        writeFile(binPath + "\\projects.html", file, footer);
        Main.LOGGER.success("Generated file: projects.html");
        copyIcons(projects);
    }

    private void copyIcon(Project project) throws IOException {
        Main.DEBUG_LOGGER.info("Copying project icon: " + project.getId());

        Path dataPath = Path.of(Main.getMainPath().toString(), "data", "projects", project.getId(), "icon.png");
        Path binPath = Path.of(this.binPath, "images", "projects", project.getId(), "icon.png");

        binPath.getParent().toFile().mkdirs();

        Files.copy(dataPath, binPath);

        Main.DEBUG_LOGGER.success("Copied project icon: " + project.getId());
    }
}
