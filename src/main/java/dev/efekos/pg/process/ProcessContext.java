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

package dev.efekos.pg.process;

import dev.efekos.pg.data.schema.*;
import dev.efekos.pg.data.timeline.TimelineEvent;

import java.util.Arrays;
import java.util.List;

public class ProcessContext {
    public EducationInfo educationInfo;
    public GeneralInfo generalInfo;
    public ExperienceInfo experienceInfo;
    public String binPath;
    public ContactInfo contactInfo;
    public List<Certificate> certificates;
    public List<Project> projects;
    public TagColorInfo tagColorInfo;
    public List<TimelineEvent> collectedTimeline;

    public List<Object> getEverything(){
        return Arrays.asList(educationInfo,generalInfo,experienceInfo,contactInfo,certificates,projects,tagColorInfo,binPath);
    }
}