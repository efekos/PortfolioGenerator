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

    public ProjectVersionPageGenerator(GeneralInfo generalInfo, Project project,String tags,String binPath) {
        this.generalInfo = generalInfo;
        this.project = project;
        this.tags = tags;
        this.binPath = binPath;
    }

    public String generate() throws IOException {
        VersionInfo versionInfo = project.getVersionInfo();
        List<String> elements = new ArrayList<>();
        List<String> scripts = new ArrayList<>();


        switch (versionInfo.getType()){
            case object -> {
                String template = Main.readStringResource("/site/project_version_entry.html");

                for (Version version : versionInfo.getVersions()) {
                    elements.add(
                            template.replaceAll("%%vrtid%%",version.getType().getId())
                            .replaceAll("%%vrtname%%",version.getType().getDisplay())
                            .replaceAll("%%vrtag%%",version.getVersion())
                            .replaceAll("%%vrdate%%",version.getReleaseDate().toString())
                    );
                }
            }
            case markdown_file -> {
                elements.add("<div class=\"markdown\" id=\"versions\"></div>");
                scripts.add("<script src=\"./versions_finder.js\" ></script>");


                System.out.println("Generating file: projects/"+project.getId()+"/versions_finder.js");

                String script = Main.readStringResource("/site/project_versions_markdown_file_finder.js") // A file named with FIVE words? That's the spirit!
                        .replaceAll("%%link%%", versionInfo.getFile());

                writeFile(binPath+"\\projects\\"+project.getId()+"\\versions_finder.js",script);
            }
            default -> System.out.println("[DEVELOPER WARNING] Not implemented version info type found: '"+versionInfo.getType()+"'");
        }

        return Main.readStringResource("/site/project_versions.html")
                .replaceAll("%%name%%",generalInfo.getName())
                .replaceAll("%%prname%%",project.getDisplayName())
                .replaceAll("%%tags%%",tags)
                .replaceAll("%%prid%%",project.getId())
                .replaceAll("%%scripts%%",String.join("\n",scripts))
                .replaceAll("%%mainElement%%",String.join("\n\n",elements));
    }
}
