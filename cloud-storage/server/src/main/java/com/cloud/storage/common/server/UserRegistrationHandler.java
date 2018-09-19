package com.cloud.storage.common.server;

import com.cloud.storage.common.ServiceCommands;
import com.cloud.storage.common.message.RegistrationMessage;
import io.netty.channel.ChannelHandlerContext;

public class UserRegistrationHandler implements ServiceCommands {
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
    }
}
