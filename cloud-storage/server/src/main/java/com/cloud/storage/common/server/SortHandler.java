package com.cloud.storage.common.server;

import com.cloud.storage.common.message.CommandMessage;
import com.cloud.storage.common.message.FileMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SortHandler extends ChannelInboundHandlerAdapter {
    private User user;

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
        else System.out.println("wrong");
    }
}
