package dev.efekos.pg;

public class Main {
    private static String MAIN_PATH;

    public static String getMainPath() {
        return MAIN_PATH;
    }

    public static void main(String[] args) {
        MAIN_PATH = System.getProperty("user.dir");
    }
}