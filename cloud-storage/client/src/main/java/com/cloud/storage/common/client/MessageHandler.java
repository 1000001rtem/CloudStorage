package com.cloud.storage.common.client;

import com.cloud.storage.common.ServiceCommands;
import com.cloud.storage.common.message.CommandMessage;
import com.cloud.storage.common.message.FileListMessage;
import com.cloud.storage.common.message.FileMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MessageHandler implements ServiceCommands {

    public static final Logger logger = LogManager.getLogger(MessageHandler.class.getName());

    private byte[] message;
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
                break;
            default:
                logger.error("Invalid Command");

        }
    }
}
