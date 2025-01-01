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

import dev.efekos.pg.output.FileGenerator;

public class GenerateFileProcess implements Process {
    @Override
    public String getName() {
        return "Generate Files";
    }

    @Override
    public void init(ProcessContext context) throws Exception {
        FileGenerator generator = new FileGenerator(context.binPath, context.footer);

        // generating
        generator.generateIndexFile(context);
        generator.generateCertificatesFile(context.generalInfo, context.certificates);
        generator.generateBioFile(context.generalInfo);
        generator.generateScriptFiles(context.generalInfo, context.contributors);
        generator.generateEducationFile(context.generalInfo, context.educationInfo);
        generator.generateExperienceFile(context.generalInfo, context.experienceInfo);
        generator.generateStyleFiles(context.generalInfo, context.tagColorInfo);
        generator.generateProjectsPage(context.generalInfo, context.projects);
        generator.generateContactPage(context.generalInfo, context.contactInfo);
        generator.copyLibraries();
        generator.copyLanguageFiles();

        // copying
        generator.copyIcons(context.generalInfo);
    }
}
