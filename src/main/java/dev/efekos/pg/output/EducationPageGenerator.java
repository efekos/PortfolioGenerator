package dev.efekos.pg.output;

import dev.efekos.pg.Main;
import dev.efekos.pg.data.schema.EducationEntry;
import dev.efekos.pg.data.schema.EducationInfo;
import dev.efekos.pg.data.schema.GeneralInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EducationPageGenerator implements Generator{
    private final String binPath;

    public EducationPageGenerator(String binPath) {
        this.binPath = binPath;
    }

    private final String EDUCATION_ENTRY_ELEMENT =
            """
                            <li>
                                <div class="entry">
                                    <div>
                                        <img src="./images/type/university.svg" alt="School" width="25" class="type" />
                                    </div>
                                    <div>
                                        <span class="title">%%pname%%</span>
                                    </div>
                                    <div>
                                        <img src="./images/type/university.svg" width="20" class="type-text"/><span class="alt">%%ptype%%</span><br>
                                        <img src="./images/type/location.svg" width="20" class="type-text"/><span class="alt">%%plocation%%</span><br>
                                        <img src="./images/type/clock.svg" width="20" class="type-text"/><span class="alt">%%pstart%% - %%pend%%</span>
                                    </div>
                                </div>
                            </li>\
                    """;

    public void generate(GeneralInfo generalInfo,EducationInfo info) throws IOException{
        System.out.println("Generating file: education.html");
        generateElements(info);
        generateFile(generalInfo,info);
        System.out.println("Generated file: education.html");
    }

    private void generateFile(GeneralInfo generalInfo,EducationInfo info) throws IOException {
        String file = Main.readStringResource("/site/education.html")
                .replaceAll("%%entries%%",String.join("",elementsGenerated))
                .replaceAll("%%name%%",generalInfo.getName());

        writeFile(binPath+"\\education.json",file);
    }

    private final List<String> elementsGenerated = new ArrayList<>();

    private void generateElements(EducationInfo info) {
        List<EducationEntry> entries = info.getEntries();
        entries.sort(Comparator.comparing(EducationEntry::getStart));

        elementsGenerated.clear();
        for (EducationEntry entry : entries) {
            String element = EDUCATION_ENTRY_ELEMENT.replaceAll("%%pname%%",entry.getTitle())
                    .replaceAll("%%ptype%%",entry.getType().getDisplay())
                    .replaceAll("%%plocation%%",entry.getLocation())
                    .replaceAll("%%pstart%%",entry.getStart().toString())
                    .replaceAll("%%pend%%",entry.getUntil().toString());

            elementsGenerated.add(element);
        }
    }
}
