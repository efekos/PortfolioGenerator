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
import dev.efekos.pg.data.type.RequiredDataType;
import dev.efekos.pg.data.type.VersionType;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public class Version implements JsonSchema{
    private DayDate releaseDate;
    private String version;
    private VersionType type;

    @Override
    public void readJson(JsonElement element, DataGrabberContext context) throws JsonParseException {
        DataTypeChecker checker = new DataTypeChecker(context.getCurrentFile());
        
        checker.expectObject(element);
        parseObject(element.getAsJsonObject(),context,checker);
    }

    private void parseObject(JsonObject object,DataGrabberContext context,DataTypeChecker checker) {

        checker.searchExceptions(object,"version", RequiredDataType.STRING);
        checker.searchExceptions(object,"type", RequiredDataType.STRING);
        if(!object.has("release_date")) throw new JsonSyntaxException("'release_date' required in '"+checker.getCurrentFile()+"'");

        // type
        Optional<VersionType> typeOptional = Arrays.stream(VersionType.values()).filter(versionType -> versionType.getId().equals(object.get("type").getAsString().toLowerCase(Locale.ROOT))).findFirst();
        if(typeOptional.isEmpty()) throw new JsonParseException("Unknown version type '"+object.get("type").getAsString()+"'");
        this.type = typeOptional.get();
        
        // version, release date
        this.version = object.get("version").getAsString();
        if(object.has("link")) this.link = object.get("link").getAsString();
        else this.link = null;
        this.releaseDate = new DayDate(0,0,0);
        releaseDate.readJson(object.get("release_date"),context);
    }

    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Version(DayDate releaseDate, String version, VersionType type) {
        this.releaseDate = releaseDate;
        this.version = version;
        this.type = type;
    }

    public DayDate getReleaseDate() {
        return releaseDate;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public VersionType getType() {
        return type;
    }

    public void setType(VersionType type) {
        this.type = type;
    }
}
