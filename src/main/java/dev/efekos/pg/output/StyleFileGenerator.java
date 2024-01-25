package dev.efekos.pg.output;

import dev.efekos.pg.Main;
import dev.efekos.pg.data.schema.GeneralInfo;
import dev.efekos.pg.data.schema.TagColorInfo;
import dev.efekos.pg.data.type.VersionType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StyleFileGenerator implements Generator{

    private final String binPath;


    public StyleFileGenerator(String binPath) {
        this.binPath = binPath;
    }

    public void generateSocialIcons(GeneralInfo info) throws IOException {
        System.out.println("Generating file: style/social_icons.css");
        List<String> generatedSelections = new ArrayList<>();
        generatedSelections.add("""
                .social-icon {
                      padding: 1rem;
                      border-radius: 10rem;
                      will-change: background-color;
                      transition: background-color 200ms;
                      background-color: #404040;
                      width: 1.5rem;
                      height: 1.5rem;
                      display: inline-block;
                  }
                  
                """);
        info.getSocialLinks().forEach((type, link) -> generatedSelections.add(
                        Main.readStringResource("/site/style/template/social_icon.css")
                        .replaceAll("%%n%%", type.getNormalColor())
                        .replaceAll("%%h%%", type.getHighlightColor())
                        .replaceAll("%%i%%", type.getId()))
        );

        writeFile(binPath + "\\style\\social_icons.css", String.join("\n", generatedSelections));
        System.out.println("Generated file: style/social_icons.css");
    }

    public void generateProjectTags(TagColorInfo tagColorInfo) throws IOException {
        System.out.println("Generating file: style/project_tags.css");
        List<String> generatedSelectors = new ArrayList<>();
        String template = Main.readStringResource("/site/style/template/style_project_tag.css");
        tagColorInfo.getColors().forEach((key, color) -> generatedSelectors.add(template.replaceAll("%%tcolor%%",color).replaceAll("%%tname%%",key)));

        writeFile(binPath+"\\style\\project_tags.css",String.join("\n",generatedSelectors));
        System.out.println("Generated file: style/project_tags.css");
    }

    public void generateProjectVersions() throws IOException{
        System.out.println("Generating file: style/versions.css");

        List<String> generatedSelectors = new ArrayList<>();

        generatedSelectors.add("""
                .version-tag {
                    font-weight:650;
                }
                """);

        String template = Main.readStringResource("/site/style/template/version_tag.css");

        for (VersionType type : VersionType.values()) {
            generatedSelectors.add(template.replaceAll("%%vrtid%%",type.getId()).replaceAll("%%vrtcolor%%",type.getColor()));
        }

        writeFile(binPath+"\\style\\versions.css",String.join("\n\n",generatedSelectors));
        System.out.println("Generated file: style/versions.css");
    }
}
