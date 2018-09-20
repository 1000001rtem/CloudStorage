package com.cloud.storage.common.client;

import com.cloud.storage.common.ServiceCommands;
import com.cloud.storage.common.message.CommandMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandHandler implements ServiceCommands {

    public static final Logger logger = LogManager.getLogger(CommandHandler.class.getName());

    private CommandMessage message;
    Controllers controller;

    public CommandHandler(CommandMessage message, Controllers controller) {
        this.controller = controller;
        this.message = message;
        if (message != null) {
            processCommand(this.message);
        }
    }

    private void processCommand(CommandMessage message) {
        MessageDecoder decoder = new MessageDecoder();
        byte command = decoder.getCommand(message);
        switch (command) {
            case ERROR:
                logger.error("Server send ERROR code");
                break;
            case WRONG_LOG_OR_PASS:
                logger.info("Wrong login or Password");
                break;
            case AUTH_SUCCESS:
                controller.changeScene();
                logger.info("Authorization success");
                break;
            case REG_SUCCESS:
                logger.info("Registration success");
                break;
            case USER_EXIST:
                logger.info("User Exist");
                break;
            case FILE_EXIST:
                logger.info("File Exist");
                break;
        }
    }
}
