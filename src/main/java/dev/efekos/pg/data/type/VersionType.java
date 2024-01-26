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

package dev.efekos.pg.data.type;

public enum VersionType {
    RELEASE("release","Release","rgb(99, 224, 118)"),
    BETA("beta","Beta","rgb(255, 111, 0)"),
    ALPHA("alpha","Alpha","rgb(255, 59, 20)"),
    PROTOTYPE("prototype","Prototype","rgb(64, 119, 230)"),
    SNAPSHOT("snapshot","Snapshot","rgb(245, 240, 93)"),
    RELEASE_CANDIDATE("rc","Release Candidate","rgb(130, 219, 94)"),
    PRE_RELEASE("pre_release","Pre-Release","rgb(110, 219, 66)")
    ;

    private final String id;
    private final String display;
    private final String color;

    VersionType(String id, String display, String color) {
        this.id = id;
        this.display = display;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    public String getColor() {
        return color;
    }
}
