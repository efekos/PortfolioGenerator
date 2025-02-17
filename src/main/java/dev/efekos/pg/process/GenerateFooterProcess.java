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

package dev.efekos.pg.process;

import dev.efekos.pg.data.schema.Contributor;
import dev.efekos.pg.resource.ResourceManager;
import dev.efekos.pg.resource.Resources;
import dev.efekos.pg.util.Text;

import java.util.List;

public class GenerateFooterProcess implements Process {
    public static String generateContributor(Contributor contributor) {
        String template = ResourceManager.getResource(Resources.HTML_CONTRIBUTOR);

        List<String> list = contributor.getCategories().stream().map(string -> "<li>" + Text.translated("contribution." + string) + "</li>").toList();

        return template
                .replace("%%avatar%%", contributor.getAvatarUrl())
                .replace("%%name%%", contributor.getName())
                .replace("%%link%%", contributor.getUrl())
                .replace("%%categories%%", list.isEmpty() ? "" : "<ul>" + String.join("", list) + "</ul>");
    }

    @Override
    public String getName() {
        return "Generate Footer";
    }

    @Override
    public void init(ProcessContext context) throws Exception {
        String template = ResourceManager.getResource(Resources.HTML_FOOTER)
                .replace("%%version%%", Text.translated("footer.version", "1.2.0"));

        List<String> contributorElements = context.contributors.stream().map(GenerateFooterProcess::generateContributor).toList();

        context.footer = template.replace("%%contributors%%", Text.translated("footer.contributed", String.join("", contributorElements)));
    }
}
