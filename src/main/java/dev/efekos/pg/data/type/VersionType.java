/*
 * Copyright 2025 efekos
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

import lombok.Getter;

@Getter
public enum VersionType {
    RELEASE("release", "Release", "releaseColor"),
    BETA("beta", "Beta", "betaColor"),
    ALPHA("alpha", "Alpha", "alphaColor"),
    PROTOTYPE("prototype", "Prototype", "prototypeColor"),
    SNAPSHOT("snapshot", "Snapshot", "snapshotColor"),
    RELEASE_CANDIDATE("rc", "Release Candidate", "releaseCandidateColor"),
    PRE_RELEASE("pre_release", "Pre-Release", "preReleaseColor");

    private final String id;
    private final String display;
    private final String color;

    VersionType(String id, String display, String color) {
        this.id = id;
        this.display = display;
        this.color = color;
    }

}
