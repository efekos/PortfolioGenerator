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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dev.efekos.pg.data.DataGrabberContext;
import dev.efekos.pg.data.type.DataTypeChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class ProjectGalleryImageList implements JsonSchema{
    private final List<ProjectGalleryImage> actualList = new ArrayList<>();

    @Override
    public void readJson(JsonElement element, DataGrabberContext context) throws JsonParseException {
        DataTypeChecker checker = new DataTypeChecker(context.getCurrentFile());

        checker.expectArray(element);
        JsonArray array = element.getAsJsonArray();
        clear();
        for (JsonElement jsonElement : array) {
            ProjectGalleryImage image = new ProjectGalleryImage();
            image.readJson(jsonElement, context);
            add(image);
        }
    }

    public int size() {
        return actualList.size();
    }

    public boolean isEmpty() {
        return actualList.isEmpty();
    }

    public boolean contains(ProjectGalleryImage o) {
        return actualList.contains(o);
    }


    public boolean add(ProjectGalleryImage projectGalleryImage) {
        return actualList.add(projectGalleryImage);
    }

    public void replaceAll(UnaryOperator<ProjectGalleryImage> operator) {
        actualList.replaceAll(operator);
    }

    public void clear() {
        actualList.clear();
    }

    public ProjectGalleryImage get(int index) {
        return actualList.get(index);
    }

    public void add(int index, ProjectGalleryImage element) {
        actualList.add(index, element);
    }

    public Stream<ProjectGalleryImage> stream() {
        return actualList.stream();
    }

    public void forEach(Consumer<? super ProjectGalleryImage> action) {
        actualList.forEach(action);
    }
}
