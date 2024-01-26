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
import com.google.gson.JsonSyntaxException;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.type.DataTypeChecker;
import dev.efekos.pg.data.type.ProjectLinkType;
import dev.efekos.pg.data.type.RequiredDataType;

import java.util.*;

public class Project implements JsonSchema {
    private String id; // nijson
    private String displayName;
    private Map<ProjectLinkType, String> links = new HashMap<>();
    private String mainWebsite;
    private String changeLogFile;
    private List<String> tags = new ArrayList<>();
    private String readmeFile; // nijson
    private String summary;
    private String version;
    private String license;
    private String fullLicense; //nijson
    private DayDate release;
    private ProjectGalleryImageList galleryImages;
    private VersionInfo versionInfo;

    @Override
    public void readJson(JsonElement element, DataGrabberContext context) throws JsonParseException {
        DataTypeChecker checker = new DataTypeChecker(context.getCurrentFile());

        checker.expectObject(element);
        JsonObject object = element.getAsJsonObject();

        checker.searchExceptions(object, "display_name", RequiredDataType.STRING);
        checker.searchExceptions(object, "links", RequiredDataType.OBJECT);
        checker.searchExceptions(object, "main_website", RequiredDataType.STRING);
        checker.searchExceptions(object, "change_log", RequiredDataType.STRING);
        checker.searchExceptions(object, "tags", RequiredDataType.ARRAY);
        checker.searchExceptions(object, "summary", RequiredDataType.STRING);
        checker.searchExceptions(object, "version", RequiredDataType.STRING);
        checker.searchExceptions(object, "license", RequiredDataType.STRING);
        checker.searchExceptions(object, "readme_link", RequiredDataType.STRING);
        checker.searchExceptions(object,"version_info",RequiredDataType.OBJECT);


        // display name,main website, changelog file, summary, version, license, readme file
        this.displayName = object.get("display_name").getAsString();
        this.mainWebsite = object.get("main_website").getAsString();
        this.changeLogFile = object.get("change_log").getAsString();
        this.summary = object.get("summary").getAsString();
        this.version = object.get("version").getAsString();
        this.license = object.get("license").getAsString();
        this.readmeFile = object.get("readme_link").getAsString();

        // release date
        if (!object.has("release"))
            throw new JsonSyntaxException("'release' required in file '" + context.getCurrentFile() + "'");
        this.release = new DayDate(0, 0, 0);
        release.readJson(object.get("release"), context);

        // links
        JsonObject linksObject = object.get("links").getAsJsonObject();
        linksObject.asMap().forEach((key, link) -> {
            Optional<ProjectLinkType> id = ProjectLinkType.findById(key);
            if(id.isEmpty()) throw new JsonParseException("Unknown link id '"+key+"'");

            if (this.links.containsKey(id.get())) throw new JsonParseException("Link id '" + key + "' used more than once");
            this.links.put(id.get(), link.getAsString());
        });

        // tags
        tags.clear();
        for (JsonElement jsonElement : object.get("tags").getAsJsonArray()) {
            tags.add(jsonElement.getAsString());
        }

        // vesion info
        JsonElement versionInfoElement = object.get("version_info");
        VersionInfo info = new VersionInfo();
        info.readJson(versionInfoElement,context);
        this.versionInfo = info;
    }

    public VersionInfo getVersionInfo() {
        return versionInfo;
    }

    public void setVersionInfo(VersionInfo versionInfo) {
        this.versionInfo = versionInfo;
    }

    public ProjectGalleryImageList getGalleryImages() {
        return galleryImages;
    }

    public void setGalleryImages(ProjectGalleryImageList galleryImages) {
        this.galleryImages = galleryImages;
    }

    public DayDate getRelease() {
        return release;
    }

    public void setRelease(DayDate release) {
        this.release = release;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Map<ProjectLinkType, String> getLinks() {
        return links;
    }

    public void setLinks(Map<ProjectLinkType, String> links) {
        this.links = links;
    }

    public String getMainWebsite() {
        return mainWebsite;
    }

    public String getFullLicense() {
        return fullLicense;
    }

    public void setFullLicense(String fullLicense) {
        this.fullLicense = fullLicense;
    }

    public void setMainWebsite(String mainWebsite) {
        this.mainWebsite = mainWebsite;
    }

    public String getChangeLogFile() {
        return changeLogFile;
    }

    public void setChangeLogFile(String changeLogFile) {
        this.changeLogFile = changeLogFile;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getReadmeFile() {
        return readmeFile;
    }

    public void setReadmeFile(String readmeFile) {
        this.readmeFile = readmeFile;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}