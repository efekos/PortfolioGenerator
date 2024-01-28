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
import dev.efekos.pg.data.schema.EducationEntry;
import dev.efekos.pg.data.schema.EducationInfo;
import dev.efekos.pg.data.schema.GeneralInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EducationPageGenerator implements Generator {
    private final String binPath;

    public EducationPageGenerator(String binPath) {
        this.binPath = binPath;
    }

    private final String EDUCATION_ENTRY_ELEMENT = Main.readStringResource("/site/html/template/education_entry.html");

    public void generate(GeneralInfo generalInfo, EducationInfo info) throws IOException {
        Main.LOGGER.info("Generating file: education.html");
        generateElements(info);
        generateFile(generalInfo);
        Main.LOGGER.success("Generated file: education.html");
    }

    private void generateFile(GeneralInfo generalInfo) throws IOException {
        Main.DEBUG_LOGGER.info("Generating file");
        String file = Main.readStringResource("/site/html/education.html")
                .replaceAll("%%entries%%", String.join("", elementsGenerated))
                .replaceAll("%%name%%", generalInfo.getName());

        writeFile(binPath + "\\education.html", file);
        Main.DEBUG_LOGGER.success("Generated file");
    }

    private final List<String> elementsGenerated = new ArrayList<>();

    private void generateElements(EducationInfo info) {
        Main.DEBUG_LOGGER.info("Generating elements");
        List<EducationEntry> entries = info.getEntries();
        entries.sort(Comparator.comparing(EducationEntry::getStart));

        elementsGenerated.clear();
        for (EducationEntry entry : entries) {
            String element = EDUCATION_ENTRY_ELEMENT.replaceAll("%%pname%%", entry.getTitle())
                    .replaceAll("%%ptype%%", entry.getType().getDisplay())
                    .replaceAll("%%plocation%%", entry.getLocation())
                    .replaceAll("%%pstart%%", entry.getStart().toString())
                    .replaceAll("%%pend%%", entry.getUntil().toString());

            elementsGenerated.add(element);
        }
        Main.DEBUG_LOGGER.success("Generated elements");
    }
}
