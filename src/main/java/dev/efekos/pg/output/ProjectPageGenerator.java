package dev.efekos.pg.output;

import dev.efekos.pg.Main;
import dev.efekos.pg.data.schema.GeneralInfo;
import dev.efekos.pg.data.schema.Project;
import dev.efekos.pg.data.schema.ProjectGalleryImage;
import org.apache.commons.io.FileUtils;

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

    private void generateScripts(Project project) throws IOException {
        generateReadmeFinder(project);
        generateGalleryModals(project);
    }

    private void generateGalleryModals(Project project) throws IOException{
        List<String> codeblocks = new ArrayList<>();
        String template = Main.readStringResource("/site/script/gallery_modal_template.js");

        project.getGalleryImages().forEach(image -> {
            String generated = template.replaceAll("%%imid%%",image.getId());
            codeblocks.add(generated);
        });

        writeFile(binPath+"\\projects\\"+project.getId()+"\\gallery_modals.js",String.join("\n\n",codeblocks));
    }

    private void generateReadmeFinder(Project project) throws IOException {
        System.out.println("Generating file: projects/" + project.getId() + "/readme_finder.js");
        String readmeFinder = Main.readStringResource("/site/script/project_readme_finder.js")
                .replaceAll("%%link%%", project.getReadmeFile());
        writeFile(binPath + "\\projects\\" + project.getId() + "\\readme_finder.js", readmeFinder);

        String file = Main.readStringResource("/site/script/project_changelog_finder.js")
                .replaceAll("%%link%%", project.getChangeLogFile());
        writeFile(binPath + "\\projects\\" + project.getId() + "\\changelog_finder.js", file);

        System.out.println("Generated file: projects/" + project.getId() + "/readme_finder.js");
    }

    public void generateSinglePage(GeneralInfo info, Project project) throws IOException {
        Path mainDirectory = Path.of(binPath, "projects", project.getId());
        Path mainDataDirectory = Path.of(Main.getMainPath().toString(), "data", "projects", project.getId());

        mainDirectory.toFile().mkdirs();

        String tags = String.join("",project.getTags().stream().map(s -> "<div class=\"project-tag-"+s+"\">"+s+"</div>").toList());

        //index.html
        String html = Main.readStringResource("/site/html/project.html")
                .replace("%%name%%", info.getName())
                .replaceAll("%%prname%%", project.getDisplayName())
                .replaceAll("%%tags%%",tags)
                .replaceAll("%%prid%%", project.getId());

        writeFile(mainDirectory + "\\index.html", html);

        //license.html
        String license = Main.readStringResource("/site/html/project_license.html")
                .replaceAll("%%name%%", info.getName())
                .replaceAll("%%prname%%", project.getDisplayName())
                .replaceAll("%%prid%%", project.getId())
                .replaceAll("%%prlicense%%", project.getLicense())
                .replaceAll("%%tags%%",tags)
                .replaceAll("%%prflicense%%", project.getFullLicense());

        writeFile(mainDirectory + "\\license.html", license);

        //changelog.html
        String changelog = Main.readStringResource("/site/html/project_changelog.html")
                .replaceAll("%%name%%", info.getName())
                .replaceAll("%%prname%%", project.getDisplayName())
                .replaceAll("%%tags%%",tags)
                .replaceAll("%%prid%%", project.getId());

        writeFile(mainDirectory + "\\changelog.html", changelog);

        //gallery.html
        String gallery = Main.readStringResource("/site/html/project_gallery.html")
                .replaceAll("%%name%%",info.getName())
                .replaceAll("%%prname%%",project.getDisplayName())
                .replaceAll("%%tags%%",tags)
                .replaceAll("%%prid%%",project.getId())
                .replaceAll("%%images%%",generateGalleryImageElements(project));

        writeFile(mainDirectory+"\\gallery.html",gallery);

        //versions.html
        writeFile(mainDirectory+"\\versions.html",new ProjectVersionPageGenerator(info,project,tags,binPath).generate());

        //assets
        Path assetsDirectory = Path.of(mainDataDirectory.toString(), "assets");
        FileUtils.copyDirectory(assetsDirectory.toFile(), Path.of(mainDirectory.toString(), "assets").toFile());

        generateScripts(project);
    }

    private String generateGalleryImageElements(Project project)  throws IOException{
        List<ProjectGalleryImage> images = project.getGalleryImages().stream().toList();
        String template = Main.readStringResource("/site/html/template/project_gallery_image_template.html");

        List<String> generatedElements = new ArrayList<>();

        for (ProjectGalleryImage image : images) {
            String generated = template
                    .replaceAll("%%impath%%", image.getFile())
                    .replaceAll("%%imid%%", image.getId())
                    .replaceAll("%%imname%%", image.getName())
                    .replaceAll("%%imdescription%%", image.getDescription());

            generatedElements.add(generated);
        }

        return String.join("\n\n",generatedElements);
    }

    public void generateSinglePages(GeneralInfo info, List<Project> projects) throws IOException {
        for (Project project : projects) {
            generateSinglePage(info, project);
        }
    }

    private void copyIcons(List<Project> projects) throws IOException {
        System.out.println("Copying project icons");

        for (Project project : projects) {
            copyIcon(project);
        }

        System.out.println("Copied project icons");
    }

    public void generateMainPage(GeneralInfo generalInfo, List<Project> projects) throws Exception {
        System.out.println("Generating file: projects.html");
        List<String> elements = new ArrayList<>();

        String elementTemplate = Main.readStringResource("/site/html/template/projects_element_template.html");
        for (Project project : projects) {
            String element = elementTemplate
                    .replaceAll("%%prname%%", project.getDisplayName())
                    .replaceAll("%%prid%%", project.getId())
                    .replaceAll("%%prversion%%", project.getVersion())
                    .replaceAll("%%prsummary%%", project.getSummary())
                    .replaceAll("%%prlicense%%", project.getLicense())
                    .replaceAll("%%name%%", generalInfo.getName())
                    .replaceAll("%%tags%%",String.join("",project.getTags().stream().map(s -> "<div class=\"project-tag-"+s+"\">"+s+"</div>").toList()))
                    .replaceAll("%%prcreatedate%%", project.getRelease().toString());
            elements.add(element);
        }

        String file = Main.readStringResource("/site/html/projects.html")
                .replaceAll("%%name%%", generalInfo.getName())
                .replaceAll("%%elements%%", String.join("", elements));

        writeFile(binPath + "\\projects.html", file);
        System.out.println("Generated file: projects.html");
        copyIcons(projects);
    }

    private void copyIcon(Project project) throws IOException {
        System.out.println("Copying project icon: " + project.getId());

        Path dataPath = Path.of(Main.getMainPath().toString(), "data", "projects", project.getId(), "icon.png");
        Path binPath = Path.of(this.binPath, "images", "projects", project.getId(), "icon.png");

        binPath.getParent().toFile().mkdirs();

        Files.copy(dataPath, binPath);

        System.out.println("Copied project icon: " + project.getId());
    }
}
