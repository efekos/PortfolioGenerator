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

package dev.efekos.pg.data.timeline;

import dev.efekos.pg.data.schema.Date;
import dev.efekos.pg.data.schema.DayDate;
import dev.efekos.pg.data.type.CertificateType;
import dev.efekos.pg.util.Text;

public class AchieveCertificateEvent implements TimelineEvent {
    private final DayDate when;
    private final String certificateName;
    private final CertificateType type;

    public AchieveCertificateEvent(DayDate when, String certificateName, CertificateType type) {
        this.when = when;
        this.certificateName = certificateName;
        this.type = type;
    }

    @Override
    public String getIcon() {
        return switch (type) {
            case SCHOOL -> "school";
            case UNIVERSITY -> "university";
            case PROJECT -> "link/source";
            case DEGREE -> "degree";
            case COURSE -> "book";
        };
    }

    @Override
    public String getTitle() {
        return Text.translated("timeline.certificate", certificateName);
    }

    @Override
    public String getTime() {
        return when.toString();
    }

    @Override
    public Date getDate() {
        return when;
    }
}
