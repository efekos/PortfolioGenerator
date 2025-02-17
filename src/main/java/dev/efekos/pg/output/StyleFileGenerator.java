/*
 * Copyright 2025 efekos
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
import dev.efekos.pg.data.schema.TagColorInfo;
import dev.efekos.pg.data.type.VersionType;
import dev.efekos.pg.resource.ResourceManager;
import dev.efekos.pg.resource.Resources;
import dev.efekos.pg.util.Placeholder;
import dev.efekos.pg.util.PlaceholderSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StyleFileGenerator implements Generator {

    private final String binPath;
    private final String footer;


    public StyleFileGenerator(String binPath, String footer) {
        this.binPath = binPath;
        this.footer = footer;
    }

    public void generateSocialIcons(GeneralInfo info) throws IOException {
        Main.DEBUG_LOGGER.info("Generating file: style/social_icons.css");
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
                ResourceManager.getResource(Resources.STYLE_SOCIAL_ICON_TEMPLATE,new PlaceholderSet()
                                .holder("n",type.getNormalColor())
                                .holder("h",type.getHighlightColor())
                                .holder("i",type.getId()))
        ));

        writeFile(binPath + "\\style\\social_icons.css", String.join("\n", generatedSelections), footer);
        Main.DEBUG_LOGGER.success("Generated file: style/social_icons.css");
    }

    public void generateProjectTags(TagColorInfo tagColorInfo) throws IOException {
        Main.DEBUG_LOGGER.info("Generating file: style/project_tags.css");
        List<String> generatedSelectors = new ArrayList<>();
        String template = ResourceManager.getResource(Resources.STYLE_PROJECT_TAG_TEMPLATE);
        tagColorInfo.getColors().forEach((key, color) -> generatedSelectors.add(template.replaceAll("%%tcolor%%", color).replaceAll("%%tname%%", key)));

        writeFile(binPath + "\\style\\project_tags.css", String.join("\n", generatedSelectors), footer);
        Main.DEBUG_LOGGER.success("Generated file: style/project_tags.css");
    }

    public void generateProjectVersions() throws IOException {
        Main.DEBUG_LOGGER.info("Generating file: style/versions.css");

        List<String> generatedSelectors = new ArrayList<>();

        generatedSelectors.add("""
                .version-tag {
                    font-weight:650;
                }
                """);

        String template = ResourceManager.getResource(Resources.STYLE_PROJECT_VERSION_TAG_TEMPLATE);

        for (VersionType type : VersionType.values()) {
            generatedSelectors.add(template.replaceAll("%%vrtid%%", type.getId()).replaceAll("%%vrtcolor%%", type.getColor()));
        }

        writeFile(binPath + "\\style\\versions.css", String.join("\n\n", generatedSelectors), footer);
        Main.DEBUG_LOGGER.success("Generated file: style/versions.css");
    }
}
