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

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
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

    public boolean contains(Object o) {
        return actualList.contains(o);
    }

    public Iterator<ProjectGalleryImage> iterator() {
        return actualList.iterator();
    }

    public Object[] toArray() {
        return actualList.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return actualList.toArray(a);
    }

    public boolean add(ProjectGalleryImage projectGalleryImage) {
        return actualList.add(projectGalleryImage);
    }

    public boolean remove(Object o) {
        return actualList.remove(o);
    }

    public boolean containsAll(Collection<ProjectGalleryImage> c) {
        return actualList.containsAll(c);
    }

    public boolean addAll(Collection<? extends ProjectGalleryImage> c) {
        return actualList.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends ProjectGalleryImage> c) {
        return actualList.addAll(index, c);
    }

    public boolean removeAll(Collection<?> c) {
        return actualList.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return actualList.retainAll(c);
    }

    public void replaceAll(UnaryOperator<ProjectGalleryImage> operator) {
        actualList.replaceAll(operator);
    }

    public void sort(Comparator<? super ProjectGalleryImage> c) {
        actualList.sort(c);
    }

    public void clear() {
        actualList.clear();
    }

    public ProjectGalleryImage get(int index) {
        return actualList.get(index);
    }

    public ProjectGalleryImage set(int index, ProjectGalleryImage element) {
        return actualList.set(index, element);
    }

    public void add(int index, ProjectGalleryImage element) {
        actualList.add(index, element);
    }

    public ProjectGalleryImage remove(int index) {
        return actualList.remove(index);
    }

    public int indexOf(Object o) {
        return actualList.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return actualList.lastIndexOf(o);
    }

    public ListIterator<ProjectGalleryImage> listIterator() {
        return actualList.listIterator();
    }

    public ListIterator<ProjectGalleryImage> listIterator(int index) {
        return actualList.listIterator(index);
    }

    public List<ProjectGalleryImage> subList(int fromIndex, int toIndex) {
        return actualList.subList(fromIndex, toIndex);
    }

    public Spliterator<ProjectGalleryImage> spliterator() {
        return actualList.spliterator();
    }

    public <T> T[] toArray(IntFunction<T[]> generator) {
        return actualList.toArray(generator);
    }

    public boolean removeIf(Predicate<? super ProjectGalleryImage> filter) {
        return actualList.removeIf(filter);
    }

    public Stream<ProjectGalleryImage> stream() {
        return actualList.stream();
    }

    public Stream<ProjectGalleryImage> parallelStream() {
        return actualList.parallelStream();
    }

    public void forEach(Consumer<? super ProjectGalleryImage> action) {
        actualList.forEach(action);
    }
}
