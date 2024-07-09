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
import dev.efekos.pg.resource.ResourceManager;
import dev.efekos.pg.resource.Resources;
import dev.efekos.pg.util.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CertificatesPageGenerator implements Generator {

    private final String binPath;

    private final String footer;

    public CertificatesPageGenerator(String binPath, String footer) {
        this.binPath = binPath;
        this.footer = footer;
    }

    private static String makeFileButton(String imageType, String imagePath) {

        return "<a href=\"../images/certificate/" + imagePath + "\" target=\"_blank\"><button class=\"btn btn-download\">" + imageType.toUpperCase(Locale.ROOT) + " <img src=\"../images/icon/external.svg\" width=\"24\"/></button></a>";
    }

    public void generateActualFile(GeneralInfo info, List<Certificate> certificates) throws IOException {
        Main.LOGGER.info("Generating file: certificates.html");

        List<String> elementList = certificates.stream().map(certificate -> "<a href=\"./certificate/" + makeIdForLink(certificate.getDisplay().getTitle()) + ".html\"><img class=\"certificate-image-small\" src=\"./images/certificate/" + certificate.getDisplay().getImage() + "\" alt=\"" + certificate.getDisplay().getTitle() + "\" />").toList();

        String fileString = ResourceManager.getResource(Resources.HTML_CERTIFICATES_PAGE)
                .replaceAll("%%name%%", info.getName())
                .replaceAll("%%title%%", info.getTitle())
                .replaceAll("%%images%%", String.join("\n", elementList));

        writeFile(binPath + "\\certificates.html", fileString, footer);
        Main.LOGGER.success("Generated file: certificates.html");
    }

    public void copyImages(List<Certificate> certificates) throws IOException {
        Main.LOGGER.info("Copying certificate images");
        for (Certificate certificate : certificates) {
            Main.DEBUG_LOGGER.info("Copying certificate images of certificate: ", certificate.getDisplay().getTitle());
            for (String value : certificate.getImages().values()) {
                Main.DEBUG_LOGGER.info("Copying certificate image: ", value);
                Path valuePath = Path.of(value);
                if (!Files.exists(valuePath)) throw new FileNotFoundException(value);

                String imagePath = value.replace(Main.getMainPath() + "\\data\\certificates", "");
                String certificatesFolderPath = binPath + "\\images\\certificate";
                Path path = Path.of(certificatesFolderPath, imagePath);
                Files.createDirectories(path.getParent());

                Files.copy(valuePath, path);
                Main.DEBUG_LOGGER.success("Copied certificate image: ", value);
            }
            Main.DEBUG_LOGGER.success("Copied certificate images of certificate: ", certificate.getDisplay().getTitle());
        }
        Main.LOGGER.success("Copied certificate images");
    }

    public void generateSinglePages(GeneralInfo info, List<Certificate> certificates) throws IOException {
        Main.LOGGER.info("Generating single certificate files");
        String resourceFile = ResourceManager.getResource(Resources.HTML_SINGLE_CERTIFICATE);

        Files.createDirectory(Path.of(binPath, "certificate"));

        for (Certificate certificate : certificates) {
            Main.DEBUG_LOGGER.info("Generating file: certificate/" + makeId(certificate.getDisplay().getTitle()) + ".html");
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
                    .replaceAll("%%cdate%%", Text.translated("certificate.achieve", certificate.getWhen().toString()))
                    .replaceAll("%%cipath%%", imagePath.replaceAll("\\\\", "/"))
                    .replaceAll("%cbuttons%", String.join("", imageButtonElements));

            String outputPath = binPath + "\\certificate\\" + makeId(certificate.getDisplay().getTitle()) + ".html";
            writeFile(outputPath, certificateFile, footer);
            Main.DEBUG_LOGGER.success("Generated file: certificate/" + makeId(certificate.getDisplay().getTitle()) + ".html");
        }
        Main.LOGGER.success("Generated single certificate files");
    }
}
