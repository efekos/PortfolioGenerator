package dev.efekos.pg.output;

import dev.efekos.pg.Main;
import dev.efekos.pg.data.schema.Project;

import java.util.ArrayList;
import java.util.List;

public class ProjectPageGenerator implements Generator {
    private final String binPath;

    public ProjectPageGenerator(String binPath) {
        this.binPath = binPath;
    }

    private void generateScript(Project project) throws Exception{
        System.out.println("Generating file: projects/"+project.getId()+"/readme_finder.js");
        String file = Main.readStringResource("/site/project_readme_finder.js")
                .replaceAll("%%link%%",project.getReadmeFile());

        writeFile(binPath+"\\projects\\"+project.getId()+"\\readme_finder.js",file);
        System.out.println("Generated file: projects/"+project.getId()+"/readme_finder.js");
    }

    public void generateMainPage(List<Project> projects) throws Exception{
        System.out.println("Generating file: projects.html");
        List<String> elements = new ArrayList<>();

        String elementTemplate = Main.readStringResource("/site/projects_element_template.html");
        for (Project project : projects) {
            String element = elementTemplate
                    .replaceAll("%%prname%%", project.getDisplayName())
                    .replaceAll("%%prversion%%", project.getVersion())
                    .replaceAll("%%prsummary%%", project.getSummary())
                    .replaceAll("%%prlicense%%", project.getLicense())
                    .replaceAll("%%prcreatedate%%", project.getRelease().toString());
            elements.add(element);
        }

        String file = Main.readStringResource("/site/projects.html")
                .replaceAll("%%elements%%",String.join("",elements));

        writeFile(binPath+"\\projects.html",file);
        System.out.println("Generated file: projects.html");
    }
}
