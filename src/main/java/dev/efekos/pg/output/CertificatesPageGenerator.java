package dev.efekos.pg.output;

import dev.efekos.pg.Main;
import dev.efekos.pg.data.schema.Certificate;
import dev.efekos.pg.data.schema.GeneralInfo;

import javax.imageio.IIOException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CertificatesPageGenerator implements Generator{

    private final String binPath;

    public CertificatesPageGenerator(String binPath) {
        this.binPath = binPath;
    }

    public void generateActualFile(GeneralInfo info, List<Certificate> certificates)throws IOException {
        System.out.println("Generating file: certificates.html");

        List<String> elementList = certificates.stream().map(certificate -> "<a href=\"./certificate/"+makeId(certificate.getDisplay().getTitle())+".html\"><img class=\"certificate-image\" src=\"./images/certificate/" + certificate.getDisplay().getImage() + "\", alt=\"" + certificate.getDisplay().getTitle() + "\"></img>").toList();

        String fileString = Main.readStringResource("/site/certificates.html")
                .replaceAll("%%name%%", info.getName())
                .replaceAll("%%title%%", info.getTitle())
                .replaceAll("%%images%%", String.join("", elementList));

        writeFile(binPath + "\\certificates.html", fileString);
        System.out.println("Generated file: certificates.html");
    }

    public void copyImages(GeneralInfo info,List<Certificate> certificates)throws IOException{
        for (Certificate certificate : certificates) {
            for (String value : certificate.getImages().values()) {
                Path valuePath = Path.of(value); // data daki dosya
                if (!Files.exists(valuePath)) throw new FileNotFoundException(value);

                String imagePath = value.replace(Main.getMainPath() + "\\data\\certificates", "");
                String certificatesFolderPath = binPath + "\\images\\certificate";
                Files.createDirectories(Path.of(certificatesFolderPath, imagePath).getParent());

                Files.copy(valuePath, Path.of(certificatesFolderPath, imagePath));
            }
        }
    }

    public void generateSinglePages(GeneralInfo info, List<Certificate> certificates) {

    }
}
