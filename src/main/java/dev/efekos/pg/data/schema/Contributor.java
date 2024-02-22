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
import dev.efekos.pg.data.DataGrabberContext;

import java.util.ArrayList;
import java.util.List;

public class Contributor implements JsonSchema{
    private String avatarUrl;
    private String name;
    private String url;
    private List<String> translated;
    private List<String> categories;

    @Override
    public void readJson(JsonElement element, DataGrabberContext context) throws JsonParseException {
        JsonObject object = element.getAsJsonObject();

        setAvatarUrl(object.get("avatar").getAsString());
        setName(object.get("name").getAsString());
        setUrl(object.get("url").getAsString());
        setTranslated(new ArrayList<>());
        setCategories(new ArrayList<>());

        for (JsonElement jsonElement : object.get("translated").getAsJsonArray()) {
            translated.add(jsonElement.getAsString());
        }


        for (JsonElement jsonElement : object.get("categories").getAsJsonArray()) {
            categories.add(jsonElement.getAsString());
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTranslated() {
        return translated;
    }

    public void setTranslated(List<String> translated) {
        this.translated = translated;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}
