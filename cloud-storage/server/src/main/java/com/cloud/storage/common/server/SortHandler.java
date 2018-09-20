package com.cloud.storage.common.server;

import com.cloud.storage.common.MessageEncoder;
import com.cloud.storage.common.ServiceCommands;
import com.cloud.storage.common.message.CommandMessage;
import com.cloud.storage.common.message.FileMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SortHandler extends ChannelInboundHandlerAdapter implements ServiceCommands {
    private User user;
    private MessageEncoder encoder;
    public static final Logger logger = LogManager.getLogger(SortHandler.class.getName());

    public SortHandler(User user) {
        this.user = user;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FileMessage) {
            new FileHandler(ctx, msg, user);
        }
        else if (msg instanceof CommandMessage){
            new CommandHandler(ctx, msg, user);
        }
        else {
            encoder=new MessageEncoder();
            logger.error("Cliend Send Invalid Message");
            ServerUtilities.sendMessageToClient(ctx, encoder.getMessage(ERROR));
        }
    }
}
