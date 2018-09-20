package com.cloud.storage.common.server;

import com.cloud.storage.common.Directorys;
import com.cloud.storage.common.server.DataBase.FileTable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class MyFile implements Directorys, Serializable {

    public static final Logger logger = LogManager.getLogger(MyFile.class.getName());

    private transient String path;
    private String fileSize;
    private String fileName;
    private transient String checkSum;
    private transient byte[] bytes;
    private transient FileTable table;

    public MyFile(String name, String checkSum, byte[] bytes) {
        this.fileName = name;
        this.checkSum = checkSum;
        this.bytes = bytes;
        this.fileSize = String.valueOf(bytes.length / 1024) + " kb";
        this.path = SERVER_DIRECTORY + "\\" + this.getFileName();
        this.table = new FileTable();
    }

    public MyFile(String fileName, long fileSize) {
        this.fileName = fileName;
        this.fileSize = String.valueOf(fileSize / 1024) + " kb";
    }


    public boolean addFile(int userId) {
        if (addFileInDB(userId)) {
            File file = new File(this.path);
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file))) {
                stream.write(this.bytes);
            } catch (FileNotFoundException e) {
                logger.error("File Write", e);
            } catch (IOException e) {
                logger.error("File Write", e);
            }
            return true;
        }
        return false;
    }

    private boolean addFileInDB(int id) {
        boolean result = table.createNewFile(this, id);
        return result;
    }

    public String getPath() {
        return this.path;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getCheckSum() {
        return this.checkSum;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public String getFileSize() {
        return this.fileSize;
    }
}
