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

package dev.efekos.pg.data.schema;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.efekos.pg.Main;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.timeline.BirthEvent;
import dev.efekos.pg.data.timeline.TimelineEvent;
import dev.efekos.pg.data.timeline.TimelineEventSource;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.RequiredDataType;
import dev.efekos.pg.data.type.SocialLinkType;
import dev.efekos.pg.util.Locale;
import dev.efekos.pg.util.LocaleHelper;

import java.util.*;

public class GeneralInfo implements JsonSchema, TimelineEventSource {
    private String name;
    private DayDate birthDate;
    private String title;
    private String nativeLanguage;
    private List<String> knownLanguages;
    private String welcomer;
    private String bio;
    private final Map<SocialLinkType, String> socialLinks = new HashMap<>();

    public GeneralInfo(String name, DayDate birthDate, String title) {
        this.name = name;
        this.birthDate = birthDate;
        this.title = title;
    }

    @Override
    public List<TimelineEvent> getEvents() {
        return List.of(new BirthEvent(birthDate));
    }

    public Locale getNativeLanguage() {
        return LocaleHelper.getLocale(nativeLanguage);
    }

    public List<Locale> getKnownLanguages() {
        return knownLanguages.stream().map(LocaleHelper::getLocale).toList();
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
        checker.searchExceptions(object, "native_language", RequiredDataType.STRING);
        checker.searchExceptions(object, "social_links", RequiredDataType.OBJECT);

        // name, title
        this.name = object.get("name").getAsString();
        this.title = object.get("title").getAsString();

        // birth
        this.birthDate = new DayDate(26, 2, 2010);
        birthDate.readJson(object.get("birth"), context);

        // native_language
        this.nativeLanguage = object.get("native_language").getAsString();
        if (LocaleHelper.isNotValid(this.nativeLanguage))
            throw new JsonParseException("Unknown native language code '" + this.nativeLanguage + "'");

        // known_languages
        try {

            checker.searchExceptions(object, "known_languages", RequiredDataType.ARRAY);
            this.knownLanguages = object
                    .get("known_languages")
                    .getAsJsonArray().asList()
                    .stream().map(JsonElement::getAsString)
                    .toList();

        } catch (Exception e) {
            this.knownLanguages = new ArrayList<>();
        }
        for (String knownLanguage : this.knownLanguages) {
            if (LocaleHelper.isNotValid(knownLanguage))
                throw new JsonParseException("Unknown known language code '" + knownLanguage + "'");
        }

        if (!this.knownLanguages.contains(this.nativeLanguage)) {
            Main.LOGGER.warn("Known languages doesn't contain native language, automatically adding native language into known languages");
            knownLanguages.add(nativeLanguage);
        }

        // social_links
        JsonObject linksObject = object.get("social_links").getAsJsonObject();
        linksObject.asMap().forEach((key, link) -> {
            if (!SocialLinkType.isValidId(key)) throw new JsonParseException("Unknown social link id '" + key + "'");

            SocialLinkType id = SocialLinkType.findById(key);

            if (socialLinks.containsKey(id))
                throw new JsonParseException("Social link id '" + key + "' used more than once");
            socialLinks.put(id, link.getAsString());
        });
    }

    public Map<SocialLinkType, String> getSocialLinks() {
        return socialLinks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DayDate getBirthDate() {
        return birthDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
