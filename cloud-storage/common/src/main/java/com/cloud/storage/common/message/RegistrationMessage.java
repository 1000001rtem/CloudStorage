package com.cloud.storage.common.message;

public class RegistrationMessage extends Message {
    public RegistrationMessage(byte[] bytes) {
        super(bytes);
    }

    @Override
    public Message getMessageObject() {
        return this;
    }
}
