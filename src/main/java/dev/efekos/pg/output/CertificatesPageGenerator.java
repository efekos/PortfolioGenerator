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
import dev.efekos.pg.data.schema.Certificate;
import dev.efekos.pg.data.schema.GeneralInfo;
import dev.efekos.pg.util.DateHelper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CertificatesPageGenerator implements Generator {

    private final String binPath;

    public CertificatesPageGenerator(String binPath) {
        this.binPath = binPath;
    }

    public void generateActualFile(GeneralInfo info, List<Certificate> certificates) throws IOException {
        System.out.println("Generating file: certificates.html");

        List<String> elementList = certificates.stream().map(certificate -> "<a href=\"./certificate/" + makeIdForLink(certificate.getDisplay().getTitle()) + ".html\"><img class=\"certificate-image-small\" src=\"./images/certificate/" + certificate.getDisplay().getImage() + "\" alt=\"" + certificate.getDisplay().getTitle() + "\" />").toList();

        String fileString = Main.readStringResource("/site/html/certificates.html")
                .replaceAll("%%name%%", info.getName())
                .replaceAll("%%title%%", info.getTitle())
                .replaceAll("%%images%%", String.join("\n", elementList));

        writeFile(binPath + "\\certificates.html", fileString);
        System.out.println("Generated file: certificates.html");
    }

    public void copyImages(List<Certificate> certificates) throws IOException {
        System.out.println("Copying certificate images");
        for (Certificate certificate : certificates) {
            for (String value : certificate.getImages().values()) {
                Path valuePath = Path.of(value); // data daki dosya
                if (!Files.exists(valuePath)) throw new FileNotFoundException(value);

                String imagePath = value.replace(Main.getMainPath() + "\\data\\certificates", "");
                String certificatesFolderPath = binPath + "\\images\\certificate";
                Path path = Path.of(certificatesFolderPath, imagePath);
                Files.createDirectories(path.getParent());

                Files.copy(valuePath, path);
            }
        }
        System.out.println("Copied certificate images");
    }

    public void generateSinglePages(GeneralInfo info, List<Certificate> certificates) throws IOException {
        System.out.println("Generating single certificate files");
        String resourceFile = Main.readStringResource("/site/html/single_certificate.html");

        Files.createDirectory(Path.of(binPath, "certificate"));

        for (Certificate certificate : certificates) {
            System.out.println("Generating file: certificate/" + makeId(certificate.getDisplay().getTitle()) + ".html");
            String imagePath = certificate.getDisplay().getImage().replace(Main.getMainPath() + "\\data\\certificates", "");

            List<String> imageButtonElements = new ArrayList<>();

            certificate.getImages().forEach((type, path) -> imageButtonElements.add(makeFileButton(type,
                    path.replace(Main.getMainPath() + "\\data\\certificates\\", "")
                            .replaceAll("\\\\", "/")
            )));


            String certificateFile = resourceFile
                    .replaceAll("%%name%%", info.getName())
                    .replaceAll("%%cname%%", certificate.getDisplay().getTitle())
                    .replaceAll("%%cdescription%%", certificate.getDisplay().getDescription())
                    .replaceAll("%%cdate%%", makeDateString(certificate))
                    .replaceAll("%%cipath%%", imagePath.replaceAll("\\\\", "/"))
                    .replaceAll("%cbuttons%", String.join("", imageButtonElements));

            String outputPath = binPath + "\\certificate\\" + makeId(certificate.getDisplay().getTitle()) + ".html";
            writeFile(outputPath, certificateFile);
            System.out.println("Generated file: certificate/" + makeId(certificate.getDisplay().getTitle()) + ".html");
        }
        System.out.println("Generated single certificate files");
    }

    private static String makeFileButton(String imageType, String imagePath) {

        return "<a href=\"../images/certificate/" + imagePath + "\" target=\"_blank\"><button class=\"btn btn-download\">" + imageType.toUpperCase(Locale.ROOT) + " <img src=\"../images/icon/external.svg\" width=\"24\"/></button></a>";
    }

    private static String makeDateString(Certificate certificate) {
        return certificate.getWhen().getDay() + " of " + DateHelper.monthToString(certificate.getWhen().getMonth()) + ", " + certificate.getWhen().getYear();
    }
}
