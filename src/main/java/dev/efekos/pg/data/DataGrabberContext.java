package dev.efekos.pg.data;

public class DataGrabberContext {
    private String currentFile;
    private final String dataPath;

    public DataGrabberContext(String currentFile, String dataPath) {
        this.currentFile = currentFile;
        this.dataPath = dataPath;
    }

    public void setCurrentFile(String currentFile) {
        this.currentFile = currentFile.replaceAll("\\\\","/");
    }

    public String getCurrentFile() {
        return currentFile;
    }

    public String getDataPath() {
        return dataPath;
    }
}
