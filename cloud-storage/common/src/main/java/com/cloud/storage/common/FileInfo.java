package com.cloud.storage.common;

import java.io.Serializable;

public class FileInfo implements Serializable {

    private String fileName;
    private String fileSize;

    public FileInfo(String fileName, long fileSize) {
        this.fileName = fileName;
        this.fileSize = this.fileSize = String.valueOf(fileSize/1024) + " kb";
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
