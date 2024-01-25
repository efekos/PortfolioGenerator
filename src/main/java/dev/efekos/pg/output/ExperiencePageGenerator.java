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

    private final String EDUCATION_ENTRY_ELEMENT =
            """
                            <li>
                                <div class="entry">
                                    <div>
                                        <img src="./images/icon/briefcase.svg" alt="Briefcase" width="25" class="icon" />
                                    </div>
                                    <div>
                                        <span class="title">%%pcompany%%</span>
                                    </div>
                                    <div>
                                        <img src="./images/icon/briefcase.svg" width="20" class="type-text"/><span class="alt">%%ppos%%</span><br>
                                        <img src="./images/icon/clock.svg" width="20" class="type-text"/><span class="alt">%%pstart%% - %%pend%%</span>
                                    </div>
                                </div>
                            </li>\
                    """;

    private final String CURRENT_JOB_ELEMENT =
            """    
                                <div class="entry">
                                    <div>
                                        <img src="./images/icon/briefcase.svg" alt="Briefcase" width="25" class="icon" />
                                    </div>
                                    <div>
                                        <span class="title">%%pcompany%%</span>
                                    </div>
                                    <div style=\"text-align:left;\">
                                        <img src="./images/icon/briefcase.svg" width="20" class="type-text"/><span class="alt">%%ppos%%</span><br>
                                        <img src="./images/icon/clock.svg" width="20" class="type-text"/><span class="alt">%%pstart%%</span>
                                    </div>
                                </div>
                    """;

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
            String element = EDUCATION_ENTRY_ELEMENT.replaceAll("%%pcompany%%", entry.getCompany())
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
