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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProjectVersionPageGenerator implements Generator{
    private final GeneralInfo generalInfo;
    private final Project project;
    private final String tags;
    private final String binPath;

    private static final String PAGINATION_ELEMENT = Main.readStringResource("/site/html/template/pagination_buttons.html");
    private static final String PLACEHOLDERED_RELEASES_ELEMENT = Main.readStringResource("/site/html/template/project_releases.html");
    private final String links;

    public ProjectVersionPageGenerator(GeneralInfo generalInfo, Project project,String tags,String binPath,String links) {
        this.generalInfo = generalInfo;
        this.project = project;
        this.tags = tags;
        this.binPath = binPath;
        this.links = links;
    }

    public String generate() throws IOException {
        VersionInfo versionInfo = project.getVersionInfo();
        List<String> elements = new ArrayList<>();
        List<String> scripts = new ArrayList<>();


        switch (versionInfo.getType()){
            case OBJECT -> {
                String template = Main.readStringResource("/site/html/template/project_version_entry.html",true);

                for (Version version : versionInfo.getVersions()) {
                    elements.add(
                            template.replaceAll("%%vrtid%%",version.getType().getId())
                            .replaceAll("%%vrtname%%",version.getType().getDisplay())
                            .replaceAll("%%vrtag%%",version.getVersion())
                            .replaceAll("%%vrdate%%",version.getReleaseDate().toString())
                                    .replaceAll("%%btn%%", version.getLink()!=null? "<div><a href=\""+version.getLink()+"\" target=\"_blank\"><button class=\"btn btn-download\">See More</button></a></div>":"")
                    );
                }
            }
            case MARKDOWN -> {
                elements.add("<div class=\"markdown\" id=\"versions\"></div>");
                scripts.add("<script src=\"./versions_finder.js\" ></script>");


                System.out.println("Generating file: projects/"+project.getId()+"/versions_finder.js");

                String script = Main.readStringResource("/site/script/project_versions_markdown_file_finder.js") // A file named with FIVE words? That's the spirit!
                        .replaceAll("%%link%%", versionInfo.getFile());

                writeFile(binPath+"\\projects\\"+project.getId()+"\\versions_finder.js",script);
            }
            case GITHUB_RELEASES -> {
                scripts.add("<script src=\"./versions_finder.js\"></script>");
                elements.add(PAGINATION_ELEMENT);
                elements.add(PLACEHOLDERED_RELEASES_ELEMENT);

                System.out.println("Generating file: projects/"+project.getId()+"/versions_finder.js");

                String script = Main.readStringResource("/site/script/project_versions_grl_finder.js")
                        .replaceAll("%%repo%%", versionInfo.getFile());

                writeFile(binPath+"\\projects\\"+project.getId()+"\\versions_finder.js",script);
            }
            case MODRINTH_VERSIONS -> {
                scripts.add("<script src=\"./versions_finder.js\"></script>");
                elements.add(PLACEHOLDERED_RELEASES_ELEMENT);

                System.out.println("Generating file: projects/"+project.getId()+"/versions_finder.js");

                String script = Main.readStringResource("/site/script/project_versions_mrl_finder.js")
                        .replaceAll("%%id%%", versionInfo.getFile());

                writeFile(binPath+"\\projects\\"+project.getId()+"\\versions_finder.js",script);
            }
            case JSON -> {
                scripts.add("<script src=\"./versions_finder.js\"></script>");
                elements.add(PLACEHOLDERED_RELEASES_ELEMENT);

                System.out.println("Generating file: projects/"+project.getId()+"/versions_finder.js");

                String script = Main.readStringResource("/site/script/project_versions_json_finder.js")
                        .replaceAll("%%link%%", versionInfo.getFile());

                writeFile(binPath+"\\projects\\"+project.getId()+"\\versions_finder.js",script);

            }
            case SPIGOTMC_VERSIONS -> {
                scripts.add("<script src=\"./versions_finder.js\"></script>");
                elements.add(PAGINATION_ELEMENT);
                elements.add(PLACEHOLDERED_RELEASES_ELEMENT);

                System.out.println("Generating file: projects/"+project.getId()+"/versions_finder.js");

                String script = Main.readStringResource("/site/script/project_versions_spig_finder.js")
                        .replaceAll("%%id%%", versionInfo.getFile());

                writeFile(binPath+"\\projects\\"+project.getId()+"\\versions_finder.js",script);
            }
            default -> System.out.println("[DEVELOPER WARNING] Not implemented version info type found: '"+versionInfo.getType()+"'");
        }

        return Main.readStringResource("/site/html/project_versions.html")
                .replaceAll("%%name%%",generalInfo.getName())
                .replaceAll("%%prname%%",project.getDisplayName())
                .replaceAll("%%tags%%",tags)
                .replaceAll("%%links%%",links)
                .replaceAll("%%prsummary%%",project.getSummary())
                .replaceAll("%%prmainwebsite%%",project.getMainWebsite())
                .replaceAll("%%prid%%",project.getId())
                .replaceAll("%%scripts%%",String.join("\n",scripts))
                .replaceAll("%%mainElement%%",String.join("\n\n",elements));
    }
}
