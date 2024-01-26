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

import java.util.Arrays;

public enum ProjectLinkType {
    ISSUES("issues", "Issue Tracker"),
    SOURCE("source", "Source"),
    SUPPORT("support", "Support"),
    DONATE("donate", "Donate"),
    WIKI("wiki", "Wiki"),
    DOCUMENTATION("doc", "Documentation"),
    GUIDE("guide", "Guide"),
    WEBSITE("main", "Official Website");
    private final String id;
    private final String display;

    ProjectLinkType(String id, String display) {
        this.id = id;
        this.display = display;
    }

    public String getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    public static ProjectLinkType findById(String id) {
        return Arrays.stream(values()).filter(socialLinkType -> socialLinkType.id.equals(id)).findFirst().get();
    }

    public static boolean isValidId(String id) {
        return Arrays.stream(values()).anyMatch(socialLinkType -> socialLinkType.id.equals(id));
    }
}