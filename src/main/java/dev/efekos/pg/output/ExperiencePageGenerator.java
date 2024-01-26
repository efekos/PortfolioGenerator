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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ExperiencePageGenerator implements Generator {
    private final String binPath;

    public ExperiencePageGenerator(String binPath) {
        this.binPath = binPath;
    }

    private final String EXPERIENCE_ENTRY_ELEMENT = Main.readStringResource("/site/html/template/experience_entry.html");

    private final String CURRENT_JOB_ELEMENT = Main.readStringResource("/site/html/template/current_job.html");

    public void generate(GeneralInfo generalInfo, ExperienceInfo info) throws IOException {
        System.out.println("Generating file: experience.html");
        generateElements(info);
        generateFile(generalInfo, info);
        System.out.println("Generated file: experience.html");
    }

    private void generateFile(GeneralInfo generalInfo, ExperienceInfo info) throws IOException {
        String file = Main.readStringResource("/site/html/experience.html")
                .replaceAll("%%entries%%", String.join("", elementsGenerated))
                .replaceAll("%%cc%%", currentJobElement)
                .replaceAll("%%ch%%", !currentJobElement.equals("") ? "<h2>Job History</h2><br>" : "")
                .replaceAll("%%name%%", generalInfo.getName());

        writeFile(binPath + "\\experience.html", file);
    }

    private final List<String> elementsGenerated = new ArrayList<>();
    private String currentJobElement = "";

    private void generateElements(ExperienceInfo info) {
        List<ExperienceEntry> entries = info.getEntries();
        entries.sort(Comparator.comparing(ExperienceEntry::getFrom));

        elementsGenerated.clear();
        for (ExperienceEntry entry : entries) {
            if (entry.isCurrentJob()) continue;
            String element = EXPERIENCE_ENTRY_ELEMENT.replaceAll("%%pcompany%%", entry.getCompany())
                    .replaceAll("%%ppos%%", entry.getPosition())
                    .replaceAll("%%pstart%%", entry.getFrom().toString())
                    .replaceAll("%%pend%%", entry.getTo().toString());

            elementsGenerated.add(element);
        }

        if (info.hasCurrentJob()) {
            ExperienceEntry entry = info.getCurrentJob();
            String element = CURRENT_JOB_ELEMENT.replaceAll("%%pcompany%%", entry.getCompany())
                    .replaceAll("%%ppos%%", entry.getPosition())
                    .replaceAll("%%pstart%%", entry.getFrom().toString())
                    .replaceAll("%%pend%%", entry.getTo().toString());

            currentJobElement = "<br><h2>Current Job</h2><br>" + element + "<br><br>";
        }
    }
}
