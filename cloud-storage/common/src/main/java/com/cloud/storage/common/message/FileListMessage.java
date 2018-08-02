package com.cloud.storage.common.message;

public class FileListMessage extends Message {

    public FileListMessage(byte[] bytes) {
        super(bytes);
    }

    @Override
    public Message getMessageObject() {
        return this;
    }
}
