package com.cloud.storage.common;

import java.io.Serializable;

public class FileInfo implements Serializable {

    private String fileName;
    private String fileSize;
    private String path;

    public FileInfo(String fileName, long fileSize, String path) {
        this.path = path;
        this.fileName = fileName;
        this.fileSize = this.fileSize = String.valueOf(fileSize/1024) + " kb";
    }

    public String getPath() {
        return path;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    @Override
    public String toString() {
        return String.format(fileName);
    }
}
