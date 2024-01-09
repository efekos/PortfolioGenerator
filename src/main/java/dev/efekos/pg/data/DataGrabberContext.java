package dev.efekos.pg.data;

public class DataGrabberContext {
    private String currentFile;
    private boolean doesCurrentFileHaveParent;
    private String currentParentFile;

    public DataGrabberContext(String currentFile) {
        this.currentFile = currentFile;
    }

    public boolean doesCurrentFileHaveParent() {
        return doesCurrentFileHaveParent;
    }

    public void setDoesCurrentFileHaveParent(boolean doesCurrentFileHaveParent) {
        this.doesCurrentFileHaveParent = doesCurrentFileHaveParent;
    }

    public String getCurrentParentFile() {
        return currentParentFile;
    }

    public void setCurrentParentFile(String currentParentFile) {
        this.currentParentFile = currentParentFile;
    }

    public String getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(String currentFile) {
        this.currentFile = currentFile;
    }
}
