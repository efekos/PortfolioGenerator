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

package dev.efekos.pg;

import com.google.gson.JsonParseException;
import dev.efekos.pg.process.Process;
import dev.efekos.pg.process.*;
import dev.efekos.pg.util.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;

public class Main {
    /**
     * Main path that the program is running.
     */
    private static String MAIN_PATH;

    public static final Logger LOGGER = new Logger();
    public static final DebugLogger DEBUG_LOGGER = new DebugLogger();

    /**
     * Returns a main path.
     *
     * @return main path that the program is running.
     */
    public static Path getMainPath() {
        return Path.of(MAIN_PATH);
    }

    private static final ProcessContext context = new ProcessContext();

    private static final List<Process> processList = Arrays.asList(
            new ReadResourcesProcess(),
            new ReadContributionDataProcess(),
            new GenerateFooterProcess(),
            new LoadLocalesProcess(),
            new SetupBinFolderProcess(),
            new GrabDataProcess(),
            new LoadColorThemeProcess(),
            new CollectTimelineEventsProcess(),
            new GenerateFileProcess()
    );

    private static boolean isDebug;

    public static void main(String[] args) throws Exception {
        List<String> list = Arrays.asList(args);

        if (!list.isEmpty() && list.get(0).equals("help")) {

            HelpProvider provider = new HelpProvider();

            if (list.size() == 1) provider.printDefault(LOGGER);
            else {
                Optional<HelpEntry> helpEntry = HelpProvider.ENTRIES.stream().filter(entry -> entry.name().equals(list.get(1))).findFirst();
                if (helpEntry.isEmpty()) {
                    LOGGER.error("Unknown command/option: " + list.get(1));
                    provider.printExit(LOGGER);
                    return;
                }

                provider.printEntry(helpEntry.get(), LOGGER);
                provider.printExit(LOGGER);
            }

            return;
        }

        isDebug = list.contains("--debug");
        DEBUG_LOGGER.setEnabled(isDebug);
        MAIN_PATH = System.getProperty("user.dir");
        System.out.println("Hello World!");

        context.dataPath = MAIN_PATH + "\\data";

        long time = new Date().getTime();

        for (Process process : processList) {
            runProcess(process);
        }

        long time2 = new Date().getTime();

        float seconds = (float) (time2 - time) / 1000;

        LOGGER.success("Done in " + seconds + "s! output has been saved to " + context.binPath);
        if (!list.contains("--auto")) {
            LOGGER.success("Press enter to exit...");
            System.in.read();
        }
    }


    private static void runProcess(Process process) {
        long time = new Date().getTime();
        LOGGER.plain(ConsoleColors.BLUE + "----------------------------------------");
        LOGGER.plain(ConsoleColors.BLUE + "[PROCESS INFO] " + ConsoleColors.RESET + "Starting '" + process.getName() + "' process");
        LOGGER.plain(ConsoleColors.BLUE + "----------------------------------------" + ConsoleColors.RESET);

        try {
            process.init(context);

            long time2 = new Date().getTime();
            float seconds = (float) (time2 - time) / 1000;
            LOGGER.plain(ConsoleColors.GREEN + "----------------------------------------");
            LOGGER.plain(ConsoleColors.GREEN + "[PROCESS SUCCESS] " + ConsoleColors.RESET + "Process '" + process.getName() + "' finished successfully in " + seconds + "s");
            LOGGER.plain(ConsoleColors.GREEN + "----------------------------------------" + ConsoleColors.RESET);
        } catch (JsonParseException | FileNotFoundException e) {
            // error is likely a json syntax error that USER made.

            LOGGER.plain(ConsoleColors.RED_BRIGHT + "----------------------------------------");
            LOGGER.error(e.getClass().getSimpleName(), ": ", e.getMessage());
            LOGGER.plain(ConsoleColors.RED_BRIGHT + "----------------------------------------");
            LOGGER.error("This error may be due to an issue with the provided JSON data or file.");
            LOGGER.error("Check the syntax and try again. If the same issue still occurs, even");
            LOGGER.error("though the syntax is correct. Open an issue about it on github.");
            LOGGER.error("https://github.com/efekos/PortfolioGenerator/issues");
            LOGGER.plain(ConsoleColors.RED_BRIGHT + "----------------------------------------");
            if (isDebug) e.printStackTrace();

            System.exit(1);
        } catch (Exception e) {
            if (isDebug) e.printStackTrace();
            else {
                LOGGER.plain(ConsoleColors.RED + "----------------------------------------");
                LOGGER.plain(ConsoleColors.RED + "[PROCESS FAIL] " + ConsoleColors.RESET + e.getClass().getSimpleName() + ": " + e.getMessage());
                LOGGER.plain(ConsoleColors.RED + "----------------------------------------" + ConsoleColors.RESET);
                LOGGER.error("Process '" + process.getName() + "' failed.");
                LOGGER.error("If you are a contributor, run with ", ConsoleColors.BLACK_BRIGHT + "--debug" + ConsoleColors.RESET, " to");
                LOGGER.error("see the stack trace instead of this message.");
                LOGGER.error("If you are a normal user, Open an issue on github.");
                LOGGER.error("https://github.com/efekos/PortfolioGenerator/issues");
                LOGGER.plain(ConsoleColors.RED + "----------------------------------------" + ConsoleColors.RESET);
            }
            System.exit(-1);
        }
    }

    /**
     * Reads a resource from the resources folder, and returns it as a {@link String}.
     *
     * @param path Path to the file you want to reach.
     * @return A file if found as {@link String}
     * @throws IOException If {@link URL#openStream()} method fails.
     */
    public static String readStringResource(String path, boolean useUtf8) throws IOException {
        DEBUG_LOGGER.info("Reading resource: ", path);
        InputStream stream = Objects.requireNonNull(Main.class.getResource(path)).openStream();
        return new String(stream.readAllBytes(), useUtf8 ? StandardCharsets.UTF_8 : Charset.defaultCharset());
    }


    public static String readStringResource(String path) {
        try {
            return readStringResource(path, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //TODO translation system in javascript (im so dumb i just realized this shit should not be hardcoded)
    //TODO text to keys in html templates (some js files has elements too)
    //TODO wait for like a few decades so someone will translate one word to russian
}