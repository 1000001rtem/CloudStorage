package com.cloud.storage.common.client;

import com.cloud.storage.common.FileInfo;
import com.cloud.storage.common.ServiceCommands;
import com.cloud.storage.common.message.CommandMessage;
import com.cloud.storage.common.message.FileListMessage;
import com.cloud.storage.common.message.FileMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class MessageHandler implements ServiceCommands {
    private byte []message;
    private MessageDecoder decoder;
    private Controllers controller;

    public MessageHandler(byte[] message, Controllers controller) {
        this.message = message;
        this.controller = controller;
        sortMessage();
    }

    private void sortMessage() {
        switch (message[0]) {
            case FILE_CODE:
                FileMessage fileMessage = new FileMessage(message);
                new FileHandler(fileMessage);
                break;
            case COMMAND_CODE:
                CommandMessage commandMessage = new CommandMessage(message);
                new CommandHandler(commandMessage, controller);
                break;
            case FILE_LIST_CODE:
                FileListMessage fileListMessage = new FileListMessage(message);
                ClientUtilities.fillServerTable(new FileListMessage(message).getBytes());
        }
    }
}
