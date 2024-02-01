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

package dev.efekos.pg.output;

import dev.efekos.pg.Main;
import dev.efekos.pg.resource.Resource;
import dev.efekos.pg.resource.ResourceManager;
import dev.efekos.pg.resource.Resources;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public interface Generator {
    default String makeId(String id) {
        return id
                .replaceAll(" ", "_")
                .toLowerCase(Locale.ROOT);
    }

    default String makeIdForLink(String id) {
        return id
                .replaceAll(" ", "_")
                .replaceAll("#", "%23")
                .toLowerCase(Locale.ROOT);
    }

    default void copyResource(Resource resource, String outputLocation, String binPath) throws IOException {
        Main.DEBUG_LOGGER.info("Copying resource: " + resource.getPathName());

        String fileString = ResourceManager.getResource(resource);
        File file = new File(binPath + outputLocation);
        file.getParentFile().mkdirs();
        file.createNewFile();

        FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8);
        writer.write(fileString);
        writer.flush();
        writer.close();

        Main.DEBUG_LOGGER.success("Copied resource: " + resource.getPathName() + " to " + outputLocation.replaceAll("\\\\", "/"));
    }

    default void writeFile(String path, String content) throws IOException {
        String logPath = path.replaceAll("/", "\\").replace(Main.getMainPath().toString(), "").replaceAll("\\\\", "/");
        Main.DEBUG_LOGGER.info("Writing file: ", logPath);
        File file = new File(path);
        file.getParentFile().mkdirs();
        file.createNewFile();

        FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8);

        writer.write(content.replaceAll("%%footer%%", ResourceManager.getResource(Resources.HTML_FOOTER)));
        writer.flush();
        writer.close();
        Main.DEBUG_LOGGER.success("Wrote file: ", logPath);
    }
}
