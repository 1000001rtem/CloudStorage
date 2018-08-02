package com.cloud.storage.common.message;

public class FileMessage extends Message {

    public FileMessage(byte[] bytes) {
        super(bytes);
    }

    @Override
    public Message getMessageObject() {
        return this;
    }
}
