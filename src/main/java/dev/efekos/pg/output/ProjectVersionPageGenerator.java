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
import dev.efekos.pg.data.schema.Version;
import dev.efekos.pg.data.schema.VersionInfo;
import dev.efekos.pg.resource.ResourceManager;
import dev.efekos.pg.resource.Resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProjectVersionPageGenerator implements Generator {
    private final GeneralInfo generalInfo;
    private final Project project;
    private final String tags;
    private final String binPath;
    private final String footer;

    private static final String PAGINATION_ELEMENT = ResourceManager.getResource(Resources.HTML_PAGINATION_BUTTONS);
    private static final String PROJECT_RELEASES_TEMPLATE = ResourceManager.getResource(Resources.HTML_PROJECT_RELEASES_ELEMENT);
    private final String links;

    public ProjectVersionPageGenerator(GeneralInfo generalInfo, Project project, String tags, String binPath, String links, String footer) {
        this.generalInfo = generalInfo;
        this.project = project;
        this.tags = tags;
        this.binPath = binPath;
        this.links = links;
        this.footer = footer;
    }

    public String generate() throws IOException {
        VersionInfo versionInfo = project.getVersionInfo();
        List<String> elements = new ArrayList<>();
        List<String> scripts = new ArrayList<>();


        switch (versionInfo.getType()) {
            case OBJECT -> {
                Main.DEBUG_LOGGER.info("Generating version entry elements for project: ", project.getId());
                String template = ResourceManager.getResource(Resources.HTML_PROJECT_VERSION_ENTRY_TEMPLATE);

                for (Version version : versionInfo.getVersions()) {
                    elements.add(
                            template.replaceAll("%%vrtid%%", version.getType().getId())
                                    .replaceAll("%%vrtname%%", version.getType().getDisplay())
                                    .replaceAll("%%vrtag%%", version.getVersion())
                                    .replaceAll("%%vrdate%%", version.getReleaseDate().toString())
                                    .replaceAll("%%btn%%", version.getLink() != null ? "<div><a href=\"" + version.getLink() + "\" target=\"_blank\"><button class=\"btn btn-download\">See More</button></a></div>" : "")
                    );
                }
                Main.DEBUG_LOGGER.success("Generated version entry elements for project: ", project.getId());
            }
            case MARKDOWN -> {
                elements.add("<div class=\"markdown\" id=\"versions\"></div>");
                scripts.add("<script src=\"./versions_finder.js\" ></script>");


                Main.DEBUG_LOGGER.info("Generating file: projects/" + project.getId() + "/versions_finder.js");

                String script = ResourceManager.getResource(Resources.SCRIPT_VERSIONS_MARKDOWN_FINDER)
                        .replaceAll("%%link%%", versionInfo.getFile());

                writeFile(binPath + "\\projects\\" + project.getId() + "\\versions_finder.js", script, footer);
                Main.DEBUG_LOGGER.success("Generated file: projects/", project.getId(), "/versions_finder.js");
            }
            case GITHUB_RELEASES -> {
                scripts.add("<script src=\"./versions_finder.js\"></script>");
                elements.add(PAGINATION_ELEMENT);
                elements.add(PROJECT_RELEASES_TEMPLATE);

                Main.DEBUG_LOGGER.info("Generating file: projects/" + project.getId() + "/versions_finder.js");

                String script = ResourceManager.getResource(Resources.SCRIPT_VERSIONS_GITHUB_RELEASE_FINDER)
                        .replaceAll("%%repo%%", versionInfo.getFile());

                writeFile(binPath + "\\projects\\" + project.getId() + "\\versions_finder.js", script, footer);
                Main.DEBUG_LOGGER.success("Generated file: projects/", project.getId(), "/versions_finder.js");
            }
            case MODRINTH_VERSIONS -> {
                scripts.add("<script src=\"./versions_finder.js\"></script>");
                elements.add(PROJECT_RELEASES_TEMPLATE);

                Main.DEBUG_LOGGER.info("Generating file: projects/" + project.getId() + "/versions_finder.js");

                String script = ResourceManager.getResource(Resources.SCRIPT_MODRINTH_RELEASE_FINDER)
                        .replaceAll("%%id%%", versionInfo.getFile());

                writeFile(binPath + "\\projects\\" + project.getId() + "\\versions_finder.js", script, footer);
                Main.DEBUG_LOGGER.success("Generated file: projects", project.getId(), "/versions_finder.js");
            }
            case JSON -> {
                scripts.add("<script src=\"./versions_finder.js\"></script>");
                elements.add(PROJECT_RELEASES_TEMPLATE);

                Main.DEBUG_LOGGER.info("Generating file: projects/" + project.getId() + "/versions_finder.js");

                String script = ResourceManager.getResource(Resources.SCRIPT_VERSIONS_JSON_FINDER)
                        .replaceAll("%%link%%", versionInfo.getFile());

                writeFile(binPath + "\\projects\\" + project.getId() + "\\versions_finder.js", script, footer);
                Main.DEBUG_LOGGER.success("Generated file: projects", project.getId(), "/versions_finder.js");
            }
            case SPIGOTMC_VERSIONS -> {
                scripts.add("<script src=\"./versions_finder.js\"></script>");
                elements.add(PAGINATION_ELEMENT);
                elements.add(PROJECT_RELEASES_TEMPLATE);

                Main.DEBUG_LOGGER.info("Generating file: projects/" + project.getId() + "/versions_finder.js");

                String script = ResourceManager.getResource(Resources.SCRIPT_SPIGOT_UPDATE_FINDER)
                        .replaceAll("%%id%%", versionInfo.getFile());

                writeFile(binPath + "\\projects\\" + project.getId() + "\\versions_finder.js", script, footer);
                Main.DEBUG_LOGGER.success("Generated file: projects", project.getId(), "/versions_finder.js");
            }
            default ->
                    Main.LOGGER.devWarn(" Not implemented version info type found: '", versionInfo.getType().toString(), "'");
        }

        return ResourceManager.getResource(Resources.HTML_PROJECT_VERSIONS_PAGE)
                .replaceAll("%%name%%", generalInfo.getName())
                .replaceAll("%%prname%%", project.getDisplayName())
                .replaceAll("%%tags%%", tags)
                .replaceAll("%%links%%", links)
                .replaceAll("%%prsummary%%", project.getSummary())
                .replaceAll("%%prmainwebsite%%", project.getMainWebsite())
                .replaceAll("%%prid%%", project.getId())
                .replaceAll("%%scripts%%", String.join("\n", scripts))
                .replaceAll("%%mainElement%%", String.join("\n\n", elements));
    }
}