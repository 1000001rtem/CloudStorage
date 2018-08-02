package com.cloud.storage.common.client;

import com.cloud.storage.common.message.FileMessage;

public class FileHandler {
    private FileMessage message;

    public FileHandler(FileMessage message) {
        this.message = message;
        if(message != null){
            createFile(this.message);
        }
    }

    private void createFile(FileMessage message) {
        MessageDecoder decoder = new MessageDecoder();
        MyFile myFile = decoder.getFile(message);
        ClientUtilities.fillClientTableList();
    }
}
