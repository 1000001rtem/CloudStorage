package com.cloud.storage.common.server;

import com.cloud.storage.common.MessageEncoder;
import com.cloud.storage.common.ServiceCommands;
import com.cloud.storage.common.message.AuthMessage;
import com.cloud.storage.common.message.RegistrationMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Arrays;

public class AuthorizationHandler extends ChannelInboundHandlerAdapter implements ServiceCommands {
    private MessageDecoder decoder = new MessageDecoder();
    private MessageEncoder encoder = new MessageEncoder();
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if(msg instanceof RegistrationMessage){
            new UserRegistrationHandler((RegistrationMessage)msg, ctx);
        }
        else if (msg instanceof AuthMessage){
            AuthMessage message = (AuthMessage) msg;

            User user  = decoder.getUser(message);
            if (user.authorization()){
                System.out.println("welcome");
                ServerUtilities.sendMessageToClient(ctx, encoder.getMessage(AUTH_SUCCESS));
                ServerUtilities.sendMessageToClient(ctx, encoder.getFileListMessage(ServerUtilities.getFileList()));
                ctx.pipeline().addLast(new SortHandler(user));
                ctx.pipeline().remove(this);
            }
            else {
                ServerUtilities.sendMessageToClient(ctx, encoder.getMessage(WRONG_LOG_OR_PASS));
            }
        }
        else {
            ServerUtilities.sendMessageToClient(ctx, encoder.getMessage(ERROR));
        }
    }
}
