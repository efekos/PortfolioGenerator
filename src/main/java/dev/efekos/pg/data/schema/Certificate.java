package dev.efekos.pg.data.schema;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import dev.efekos.pg.Main;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.type.CertificateType;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.RequiredDataType;

import java.util.*;

public class Certificate implements JsonSchema {
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList("png", "jpg", "jpeg", "pdf");
    private Map<String, String> images;
    private DayDate when;
    private CertificateType certificateType;
    private CertificateDisplay display;

    @Override
    public void readJson(JsonElement element, DataGrabberContext context) throws JsonParseException {
        DataTypeChecker checker = new DataTypeChecker(context.getCurrentFile());
        checker.expectObject(element);
        JsonObject object = element.getAsJsonObject();

        checker.searchExceptions(object, "images", RequiredDataType.OBJECT);
        checker.searchExceptions(object, "type", RequiredDataType.STRING);
        checker.searchExceptions(object, "display", RequiredDataType.OBJECT);

        // type
        Optional<CertificateType> type = Arrays.stream(CertificateType.values()).filter(t -> t.getId().equalsIgnoreCase(object.get("type").getAsString())).findFirst();
        if (!type.isPresent())
            throw new JsonParseException("Unknown certificate type '" + object.get("type").getAsString() + "'");

        setCertificateType(type.get());

        // when
        DayDate date = new DayDate(0, 0, 0);
        date.readJson(object.get("when"), context);
        setWhen(date);

        // display
        CertificateDisplay certificateDisplay = new CertificateDisplay("", "", "");
        certificateDisplay.readJson(object.get("display"),context);
        setDisplay(certificateDisplay);

        // images
        Map<String, JsonElement> imagesMap = object.get("images").getAsJsonObject().asMap();
        List<String> seenTypeKeys = new ArrayList<>();
        this.images = new HashMap<>();

        imagesMap.forEach((typeKey, jsonElement) -> {
            if (!ALLOWED_IMAGE_TYPES.contains(typeKey.toLowerCase(Locale.ROOT)))
                throw new JsonParseException("Image type '" + typeKey + "' not allowed");
            if (seenTypeKeys.contains(typeKey.toLowerCase(Locale.ROOT)))
                throw new JsonParseException("Already saw image type '" + typeKey + "'");
            seenTypeKeys.add(typeKey);

            if (!jsonElement.isJsonPrimitive()) throw new JsonSyntaxException("Values of 'images' must be string");

            String path = toImagePath(jsonElement.getAsString());
            this.images.put(typeKey, path);

        });
    }


    private String toImagePath(String path) {
        return Main.getMainPath() + "\\data\\certificates\\" + path.replaceAll("/", "\\\\");
    }

    public Certificate() {
    }

    public Map<String, String> getImages() {
        return images;
    }

    public void setImages(Map<String, String> images) {
        this.images = images;
    }

    public DayDate getWhen() {
        return when;
    }

    public void setWhen(DayDate when) {
        this.when = when;
    }

    public CertificateType getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(CertificateType certificateType) {
        this.certificateType = certificateType;
    }

    public CertificateDisplay getDisplay() {
        return display;
    }

    public void setDisplay(CertificateDisplay display) {
        this.display = display;
    }
}
