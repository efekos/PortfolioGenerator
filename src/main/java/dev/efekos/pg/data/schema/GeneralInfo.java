package dev.efekos.pg.data.schema;

import com.google.gson.*;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.RequiredDataType;
import dev.efekos.pg.util.LocaleHelper;
import dev.efekos.pg.util.Locale;


import java.util.ArrayList;
import java.util.List;

public class GeneralInfo implements JsonSchema {
    private String name;
    private DayDate birthDate;
    private String title;
    private String nativeLanguage;
    private List<String> knownLanguages;
    private String welcomer;
    private String bio;
    private List<SocialLink> socialLinks;

    public GeneralInfo(String name, DayDate birthDate, String title) {
        this.name = name;
        this.birthDate = birthDate;
        this.title = title;
    }

    public Locale getNativeLanguage() {
        return LocaleHelper.getLocale(nativeLanguage);
    }

    public void setNativeLanguage(Locale nativeLanguage) {
        this.nativeLanguage = nativeLanguage.code();
    }

    public List<Locale> getKnownLanguages() {
        return knownLanguages.stream().map(LocaleHelper::getLocale).toList();
    }

    public void setKnownLanguages(List<Locale> knownLanguages) {
        this.knownLanguages = knownLanguages.stream().map(Locale::code).toList();
    }

    public String getWelcomer() {
        return welcomer;
    }

    public void setWelcomer(String welcomer) {
        this.welcomer = welcomer;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public GeneralInfo() {
    }

    public String getName() {
        return name;
    }

    @Override
    public void readJson(JsonElement element, DataGrabberContext context) {
        DataTypeChecker checker = new DataTypeChecker(context.getCurrentFile());
        checker.expectObject(element);
        JsonObject object = element.getAsJsonObject();

        checker.searchExceptions(object, "name", RequiredDataType.STRING);
        checker.searchExceptions(object, "title", RequiredDataType.STRING);
        checker.searchExceptions(object, "birth", RequiredDataType.OBJECT);
        checker.searchExceptions(object, "native_language", RequiredDataType.STRING);
        checker.searchExceptions(object, "social_links", RequiredDataType.ARRAY);

        // name, title
        this.name = object.get("name").getAsString();
        this.title = object.get("title").getAsString();

        // birth
        this.birthDate = new DayDate(26, 2, 2010);
        birthDate.readJson(object.get("birth").getAsJsonObject(), context);

        // native_language
        this.nativeLanguage = object.get("native_language").getAsString();
        if(!LocaleHelper.isValid(this.nativeLanguage)) throw new JsonParseException("Unknown native language code '"+this.nativeLanguage+"'");

        // known_languages
        try {

            checker.searchExceptions(object, "known_languages", RequiredDataType.ARRAY);
            this.knownLanguages = object
                    .get("known_languages")
                    .getAsJsonArray().asList()
                    .stream().map(jsonElement -> jsonElement.getAsString())
                    .toList();

        } catch (Exception e) {
            this.knownLanguages = new ArrayList<>();
        }
        for (String knownLanguage : this.knownLanguages) {
            if(!LocaleHelper.isValid(knownLanguage)) throw new JsonParseException("Unknown known language code '"+knownLanguage+"'");
        }

        if(!this.knownLanguages.contains(this.nativeLanguage)) {
            System.out.println("[WARNING] Known languages doesn't contain native language, automatically adding native language into known languages");
            knownLanguages.add(nativeLanguage);
        }


        // social_links
        JsonArray array = object.get("social_links").getAsJsonArray();
        List<SocialLink> links = new ArrayList<>();
        for (JsonElement element1 : array) {
            SocialLink link = new SocialLink(null, null);
            link.readJson(element1, context);
            links.add(link);
        }
        this.socialLinks = links;
    }

    public List<SocialLink> getSocialLinks() {
        return socialLinks;
    }

    public void setSocialLinks(List<SocialLink> socialLinks) {
        this.socialLinks = socialLinks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DayDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(DayDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
