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

import dev.efekos.pg.data.DataGrabber;
import dev.efekos.pg.data.schema.*;
import dev.efekos.pg.output.FileGenerator;
import dev.efekos.pg.process.*;
import dev.efekos.pg.process.Process;
import dev.efekos.pg.util.ConsoleColors;
import dev.efekos.pg.util.LocaleHelper;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Main {
    /**
     * Main path that the program is running.
     */
    private static String MAIN_PATH;

    public static String FOOTER_ELEMENT;

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
            new LoadLocalesProcess(),
            new SetupBinFolderProcess(),
            new GrabDataProcess(),
            new CollectTimelineEventsProcess(),
            new GenerateFileProcess()
    );

    private static boolean isDebug;

    public static void main(String[] args) throws Exception {
        isDebug = Arrays.asList(args).contains("--debug");
        FOOTER_ELEMENT = readStringResource("/site/html/template/footer.html");
        MAIN_PATH = System.getProperty("user.dir");
        System.out.println("Hello World!");

        long time = new Date().getTime();

        for (int i = 0; i < processList.size(); i++) {
            Process process = processList.get(i);
            runProcess(process);
        }


        long time2 = new Date().getTime();

        float seconds = (float) (time2 - time) / 1000;

        System.out.println(ConsoleColors.GREEN+"Done in " + seconds + "s! output has been saved to " + context.binPath);
    }


    private static void runProcess(Process process){
        long time = new Date().getTime();
        System.out.println("----------------------------------------");
        System.out.println("Starting '"+process.getName()+"' process");
        System.out.println("----------------------------------------");

        try {
            process.init(context);

            long time2 = new Date().getTime();
            float seconds = (float) (time2-time)/1000;
            System.out.println("----------------------------------------");
            System.out.println("Process '"+process.getName()+"' finished successfully in "+seconds+"s");
            System.out.println("----------------------------------------");
        } catch (Exception e){
            if(isDebug) e.printStackTrace();
            else {
                System.out.println("----------------------------------------");
                System.out.println(ConsoleColors.RED+"[PROCESS FAIL] "+ConsoleColors.RESET+e.getClass().getSimpleName()+": "+e.getMessage());
                System.out.println("----------------------------------------");
                System.out.println("Process '"+process.getName()+"' failed.");
                System.out.println("If you are a contributor, run with --debug to");
                System.out.println("see the stack trace. If you are a normal user,");
                System.out.println("Open an issue on github.");
                System.out.println("----------------------------------------");
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
    public static String readStringResource(String path,boolean useUtf8) throws IOException {
        InputStream stream = Objects.requireNonNull(Main.class.getResource(path)).openStream();
        return new String(stream.readAllBytes(),useUtf8?StandardCharsets.UTF_8:Charset.defaultCharset());
    }


    public static String readStringResource(String path) {
        try {
            return readStringResource(path,true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}