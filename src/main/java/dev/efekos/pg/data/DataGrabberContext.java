package dev.efekos.pg.data;

public class DataGrabberContext {
    private String currentFile;

    public DataGrabberContext(String currentFile) {
        this.currentFile = currentFile;
    }

    public void setCurrentFile(String currentFile) {
        this.currentFile = currentFile;
    }

    public String getCurrentFile() {
        return currentFile;
    }
}
