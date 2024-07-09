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

package dev.efekos.pg.resource;

import dev.efekos.pg.Main;

import java.util.HashMap;

public class ResourceManager {
    private static final HashMap<Resource, String> resourceMap = new HashMap<>();

    public static String getResource(Resource resource) {
        return resourceMap.get(resource);
    }

    public void init() {
        Main.LOGGER.info("Loading resources");
        Resources.all().forEach(this::putResource);
        Main.LOGGER.success("Loaded " + resourceMap.size() + " resources");
    }

    private void putResource(Resource resource) {
        String s = Main.readStringResource(resource.getPathName());
        if (resource.shouldRemoveCopyright()) {
            String CSS_COPYRIGHT_TEXT = """
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
                     */""";
            String HTML_COPYRIGHT_TEXT = """
                    <!--
                      ~ Copyright 2024 efekos
                      ~
                      ~ Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
                      ~ documentation files (the “Software”), to deal in the Software without restriction, including without limitation the
                      ~ rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
                      ~ persons to whom the Software is furnished to do so, subject to the following conditions:
                      ~
                      ~ The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
                      ~
                      ~ THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
                      ~ WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
                      ~ COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
                      ~ OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
                      -->""";

            s = s.replace(CSS_COPYRIGHT_TEXT, "").replace(HTML_COPYRIGHT_TEXT, "");
        }
        resourceMap.put(resource, s);
    }
}
