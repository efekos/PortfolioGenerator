package dev.efekos.pg.output;

import dev.efekos.pg.Main;
import dev.efekos.pg.data.schema.GeneralInfo;
import dev.efekos.pg.data.schema.Project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ProjectPageGenerator implements Generator {
    private final String binPath;

    public ProjectPageGenerator(String binPath) {
        this.binPath = binPath;
    }

    private void generateScript(Project project) throws IOException{
        System.out.println("Generating file: projects/"+project.getId()+"/readme_finder.js");
        String file = Main.readStringResource("/site/project_readme_finder.js")
                .replaceAll("%%link%%",project.getReadmeFile());

        writeFile(binPath+"\\projects\\"+project.getId()+"\\readme_finder.js",file);
        System.out.println("Generated file: projects/"+project.getId()+"/readme_finder.js");
    }

    public void generateSinglePage(GeneralInfo info,Project project) throws IOException{
        Path mainDirectory = Path.of(binPath, "projects", project.getId());

        Files.createDirectory(mainDirectory);

        String html = Main.readStringResource("/site/project.html")
                .replace("%%name%%",info.getName())
                .replaceAll("%%prname%%",project.getDisplayName())
                .replaceAll("%%prid%%",project.getId());

        generateScript(project);
    }

    public void generateSinglePages(GeneralInfo info,List<Project> projects)throws IOException{
        for (Project project : projects) {
            generateSinglePage(info,project);
        }
    }

    private void copyIcons(List<Project> projects) throws IOException{
        System.out.println("Copying project icons");

        for (Project project : projects) {
            copyIcon(project);
        }

        System.out.println("Copied project icons");
    }

    public void generateMainPage(GeneralInfo generalInfo,List<Project> projects) throws Exception{
        System.out.println("Generating file: projects.html");
        List<String> elements = new ArrayList<>();

        String elementTemplate = Main.readStringResource("/site/projects_element_template.html");
        for (Project project : projects) {
            String element = elementTemplate
                    .replaceAll("%%prname%%", project.getDisplayName())
                    .replaceAll("%%prid%%", project.getId())
                    .replaceAll("%%prversion%%", project.getVersion())
                    .replaceAll("%%prsummary%%", project.getSummary())
                    .replaceAll("%%prlicense%%", project.getLicense())
                    .replaceAll("%%name%%",generalInfo.getName())
                    .replaceAll("%%prcreatedate%%", project.getRelease().toString());
            elements.add(element);
        }

        String file = Main.readStringResource("/site/projects.html")
                .replaceAll("%%elements%%",String.join("",elements));

        writeFile(binPath+"\\projects.html",file);
        System.out.println("Generated file: projects.html");
        copyIcons(projects);
    }

    private void copyIcon(Project project) throws IOException {
        System.out.println("Copying project icon: "+project.getId());

        Path dataPath = Path.of(Main.getMainPath().toString(), "data", "projects", project.getId(), "icon.png");
        Path binPath = Path.of(this.binPath, "images", "projects", project.getId(), "icon.png");

        binPath.getParent().toFile().mkdirs();

        Files.copy(dataPath,binPath);

        System.out.println("Copied project icon: "+project.getId());
    }
}
