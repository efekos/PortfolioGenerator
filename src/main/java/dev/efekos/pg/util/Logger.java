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

import java.time.LocalTime;

public class Logger {
    public void plain(String... text) {
        System.out.println(getDate() + ConsoleColors.RESET + String.join("", text));
    }

    public void info(String... text) {
        System.out.println(getDate() + ConsoleColors.BLUE_BRIGHT + "[INFO] " + ConsoleColors.RESET + String.join("", text));
    }

    public void success(String... text) {
        System.out.println(getDate() + ConsoleColors.GREEN_BRIGHT + "[SUCCESS] " + ConsoleColors.RESET + String.join("", text));
    }

    public void error(String... text) {
        System.out.println(getDate() + ConsoleColors.RED_BRIGHT + "[ERROR] " + ConsoleColors.RESET + String.join("", text));
    }

    public void warn(String... text) {
        System.out.println(getDate() + ConsoleColors.YELLOW_BRIGHT + "[WARNING] " + ConsoleColors.RESET + String.join("", text));
    }


    public void devWarn(String... text) {
        System.out.println(getDate() + ConsoleColors.YELLOW + "[DEVELOPER WARNING] " + ConsoleColors.RESET + String.join("", text));
    }

    public String getDate() {
        LocalTime now = LocalTime.now();

        return ConsoleColors.BLUE + "[" + now.getHour() + ":" + now.getMinute() + ":" + now.getSecond() + "] ";
    }
}
