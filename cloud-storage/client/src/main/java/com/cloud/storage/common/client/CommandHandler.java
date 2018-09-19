package com.cloud.storage.common.client;

import com.cloud.storage.common.ServiceCommands;
import com.cloud.storage.common.message.CommandMessage;

public class CommandHandler implements ServiceCommands {
    private CommandMessage message;
    Controllers controller;

    public CommandHandler(CommandMessage message, Controllers controller) {
        this.controller = controller;
        this.message = message;
        if(message != null){
            processCommand (this.message);
        }
    }

//    byte ERROR = 1;
//    byte WRONG_LOG_OR_PASS = 2;
//    byte AUTH_SUCCESS = 3;
//    byte REG_SUCCESS = 4;
//    byte USER_EXIST = 5;
//    byte FILE_EXIST = 6;

    private void processCommand(CommandMessage message) {
        MessageDecoder decoder = new MessageDecoder();
        byte command = decoder.getCommand(message);
        switch (command){
            case ERROR:
                System.out.println("ERROR");
                break;
            case WRONG_LOG_OR_PASS:
                System.out.println("Wrong login or Password");
                break;
            case AUTH_SUCCESS:
                controller.changeScene();
                System.out.println("Welcome");
                break;
            case REG_SUCCESS:
                System.out.println("Registration success, please log in");
                break;
            case USER_EXIST:
                System.out.println("User Exist");
                break;
            case FILE_EXIST:
                System.out.println("File Exist");
                break;
        }
    }
}
