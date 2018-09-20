package com.cloud.storage.common.client;

import com.cloud.storage.common.message.FileMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileHandler {

    public static final Logger logger = LogManager.getLogger(MessageHandler.class.getName());

    private FileMessage message;

    public FileHandler(FileMessage message) {
        this.message = message;
        if (message != null) {
            createFile(this.message);
        }
    }

    private void createFile(FileMessage message) {
        MessageDecoder decoder = new MessageDecoder();
        MyFile myFile = decoder.getFile(message);
        ClientUtilities.fillClientTableList();
        logger.info("Server send file: " + myFile.getFileName());
    }
}
