package com.cloud.storage.common.server;

import com.cloud.storage.common.ServiceCommands;
import com.cloud.storage.common.message.RegistrationMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserRegistrationHandler implements ServiceCommands {

    public static final Logger logger = LogManager.getLogger(UserRegistrationHandler.class.getName());

    private User user;
    private ChannelHandlerContext ctx;
    private RegistrationMessage message;
    private MessageDecoder decoder;

    public UserRegistrationHandler (RegistrationMessage message, ChannelHandlerContext ctx){
        this.message = message;
        this.ctx = ctx;
        this.decoder = new MessageDecoder();

        this.user = decoder.getUser(message);
        user.createNewUser();
        logger.info("Create new user: " + user.getId() + ". " + user.getNickName());
    }
}
