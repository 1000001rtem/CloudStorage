package com.cloud.storage.common.message;

public class AuthMessage extends Message{

    public AuthMessage(byte[] bytes) {
        super(bytes);
    }

    @Override
    public Message getMessageObject() {
        return this;
    }
}
