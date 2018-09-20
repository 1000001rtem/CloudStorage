package com.cloud.storage.common.client;

import com.cloud.storage.common.Directorys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class MyFile implements Directorys {

    public static final Logger logger = LogManager.getLogger(MyFile.class.getName());

    //todo: do ref

    private String fileName;
    private String fileSize;
    private String path;
    private String checkSum;
    private byte[] bytes;

    public MyFile(String fileName, String checkSum, byte[] bytes) {
        this.fileName = fileName;
        this.checkSum = checkSum;
        this.bytes = bytes;
        this.fileSize = String.valueOf(bytes.length / 1024) + " kb";
        this.path = CLIENT_DIRECTORY + "\\" + this.getFileName();
        addFile();
    }

    public MyFile(String fileName, long fileSize) {
        this.fileName = fileName;
        this.fileSize = String.valueOf(fileSize / 1024) + " kb";
    }

    private void addFile() {
        File file = new File(this.path);
        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file))) {
            stream.write(this.bytes);
        } catch (FileNotFoundException e) {
            logger.error("Create File", e);
        } catch (IOException e) {
            logger.error("Create File", e);
        }
    }

    public String getPath() {
        return this.path;
    }

    public String getCheckSum() {
        return this.checkSum;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getFileSize() {
        return this.fileSize;
    }

}
