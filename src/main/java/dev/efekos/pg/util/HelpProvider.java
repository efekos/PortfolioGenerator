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

package dev.efekos.pg.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class HelpProvider {
    public static final List<HelpEntry> ENTRIES = Arrays.asList(
            new HelpEntry("help","Opens this menu.","Opens up a basic help menu. For more\nspecific help about an option or command, use \n"+ConsoleColors.YELLOW+"help [option/command]"+ConsoleColors.RESET+".",""),
            new HelpEntry("--auto","Automatically closes the program.","Finishes the process without asking you\nto press a key. Useful when you are using a\n.bat file to run the application.",ConsoleColors.BLACK_BRIGHT),
            new HelpEntry("--debug","Debug mode.","Shows a lot more information that may\nbe useful for debugging. Also shows you stack\ntraces to find out what the reason of a bug was.",ConsoleColors.BLACK_BRIGHT)
    );

    public void printDefault(Logger logger) throws IOException {
        printHeader(logger);
        ENTRIES.forEach(entry -> printShortEntry(entry,logger));
        printExit(logger);
    }

    public void printExit(Logger logger) throws IOException {
        logger.info("");
        logger.info("Press enter to exit...");
        System.in.read();
    }

    public void printEntry(HelpEntry entry,Logger logger){
        List<String> descLines = Arrays.asList(entry.longDesc().split("\\n"));

        logger.info(entry.colorChar()+entry.name()+ConsoleColors.RESET+": "+descLines.get(0));
        for (int i = 1; i < descLines.size(); i++) {
            logger.info(descLines.get(i));
        }
    }

    public void printShortEntry(HelpEntry entry,Logger logger){
        logger.info(entry.colorChar()+entry.name()+ConsoleColors.RESET+": "+entry.shortDesc());
    }

    public void printHeader(Logger logger){
        logger.info(ConsoleColors.WHITE_BOLD+"Welcome to PortfolioGenerator!");
        logger.info("You can run the application without any options");
        logger.info("to run normally.");
        logger.info("");
    }
}