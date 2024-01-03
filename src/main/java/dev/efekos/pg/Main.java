package dev.efekos.pg;

import dev.efekos.pg.data.DataGrabber;
import dev.efekos.pg.data.schema.GeneralInfo;

import java.nio.file.Path;

public class Main {
    private static String MAIN_PATH;

    public static Path getMainPath() {
        return Path.of(MAIN_PATH);
    }

    public static void main(String[] args)throws Exception {
        MAIN_PATH = System.getProperty("user.dir");

        DataGrabber grabber = new DataGrabber(MAIN_PATH);

        GeneralInfo generalInfo = grabber.grabGeneralInfo();
    }
}