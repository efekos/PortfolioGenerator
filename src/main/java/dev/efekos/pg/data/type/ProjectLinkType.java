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

import dev.efekos.pg.util.Text;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum ProjectLinkType {
    ISSUES("issues", "project.link.issues"),
    SOURCE("source", "project.link.source"),
    SUPPORT("support", "project.link.support"),
    WIKI("wiki", "project.link.wiki"),
    DOCUMENTATION("doc", "project.link.documentation"),
    GUIDE("guide", "project.link.guide");
    private final String id;
    private final String key;

    ProjectLinkType(String id, String key) {
        this.id = id;
        this.key = key;
    }

    public String getDisplay() {
        return Text.translated(key);
    }

    public static Optional<ProjectLinkType> findById(String id) {
        return Arrays.stream(values()).filter(socialLinkType -> socialLinkType.id.equals(id)).findFirst();
    }

}