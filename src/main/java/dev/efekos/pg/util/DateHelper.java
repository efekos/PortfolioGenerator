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

package dev.efekos.pg.util;

public class DateHelper {
    public static String monthToString(int month) {
        return switch (month) {
            default -> Text.translated("month.jan");
            case 2 -> Text.translated("month.feb");
            case 3 -> Text.translated("month.mar");
            case 4 -> Text.translated("month.apr");
            case 5 -> Text.translated("month.may");
            case 6 -> Text.translated("month.jun");
            case 7 -> Text.translated("month.jul");
            case 8 -> Text.translated("month.aug");
            case 9 -> Text.translated("month.sep");
            case 10 -> Text.translated("month.oct");
            case 11 -> Text.translated("month.nov");
            case 12 -> Text.translated("month.dec");
        };
    }

    public static String getThing(int day) {
        String stringDay = day + "";
        if (stringDay.endsWith("1")) return Text.translated("day.first", day + "");
        if (stringDay.endsWith("2")) return Text.translated("day.second", day + "");
        if (stringDay.endsWith("3")) return Text.translated("day.third", day + "");
        return Text.translated("day.other", day + "");
    }
}
