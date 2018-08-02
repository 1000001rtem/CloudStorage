package com.cloud.storage.common.message;

public class CommandMessage extends Message {

    public CommandMessage(byte[] bytes) {
        super(bytes);
    }

    @Override
    public Message getMessageObject() {
        return this;
    }
}
