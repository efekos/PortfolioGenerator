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
import dev.efekos.pg.data.schema.ExperienceEntry;
import dev.efekos.pg.data.schema.ExperienceInfo;
import dev.efekos.pg.data.schema.GeneralInfo;
import dev.efekos.pg.resource.ResourceManager;
import dev.efekos.pg.resource.Resources;
import dev.efekos.pg.util.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ExperiencePageGenerator implements Generator {
    private final String binPath;
    private final String footer;

    public ExperiencePageGenerator(String binPath, String footer) {
        this.binPath = binPath;
        this.footer = footer;
    }

    public void generate(GeneralInfo generalInfo, ExperienceInfo info) throws IOException {
        Main.LOGGER.info("Generating file: experience.html");
        generateElements(info);
        generateFile(generalInfo);
        Main.LOGGER.success("Generated file: experience.html");
    }

    private void generateFile(GeneralInfo generalInfo) throws IOException {
        Main.DEBUG_LOGGER.info("Generating file");
        String file = ResourceManager.getResource(Resources.HTML_EXPERIENCE_PAGE)
                .replaceAll("%%entries%%", String.join("", elementsGenerated))
                .replaceAll("%%cc%%", currentJobElement)
                .replaceAll("%%ch%%", !currentJobElement.isEmpty() ? "<h2>" + Text.translated("title.experience.history") + "</h2><br>" : "")
                .replaceAll("%%name%%", generalInfo.getName());

        writeFile(binPath + "\\experience.html", file, footer);
        Main.DEBUG_LOGGER.success("Generated file");
    }

    private final List<String> elementsGenerated = new ArrayList<>();
    private String currentJobElement = "";

    private void generateElements(ExperienceInfo info) {
        Main.DEBUG_LOGGER.info("Generating elements");
        List<ExperienceEntry> entries = info.getEntries();
        entries.sort(Comparator.comparing(ExperienceEntry::getFrom));

        elementsGenerated.clear();
        for (ExperienceEntry entry : entries) {
            if (entry.isCurrentJob()) continue;
            String element = ResourceManager.getResource(Resources.HTML_EXPERIENCE_ENTRY_TEMPLATE).replaceAll("%%pcompany%%", entry.getCompany())
                    .replaceAll("%%ppos%%", entry.getPosition())
                    .replaceAll("%%pstart%%", entry.getFrom().toString())
                    .replaceAll("%%pend%%", entry.getTo().toString());

            elementsGenerated.add(element);
        }

        if (info.hasCurrentJob()) {
            ExperienceEntry entry = info.getCurrentJob();
            String element = ResourceManager.getResource(Resources.HTML_CURRENT_JOB_TEMPLATE).replaceAll("%%pcompany%%", entry.getCompany())
                    .replaceAll("%%ppos%%", entry.getPosition())
                    .replaceAll("%%pstart%%", entry.getFrom().toString())
                    .replaceAll("%%pend%%", entry.getTo().toString());

            currentJobElement = "<br><h2>" + Text.translated("title.experience.current") + "</h2><br>" + element + "<br><br>";
        }
        Main.DEBUG_LOGGER.success("Generated elements");
    }
}
