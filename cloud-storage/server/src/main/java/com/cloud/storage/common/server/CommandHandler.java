package com.cloud.storage.common.server;

import com.cloud.storage.common.Directorys;
import com.cloud.storage.common.MessageEncoder;
import com.cloud.storage.common.ServiceCommands;
import com.cloud.storage.common.message.CommandMessage;
import io.netty.channel.ChannelHandlerContext;

import java.io.File;

public class CommandHandler implements ServiceCommands, Directorys {
    private ChannelHandlerContext ctx;
    private CommandMessage message;
    private MessageDecoder decoder;
    private MessageEncoder encoder;

    public CommandHandler(ChannelHandlerContext ctx, Object msg, User user) {
        this.ctx = ctx;
        this.message = (CommandMessage) msg;
        this.decoder = new MessageDecoder();
        this.encoder = new MessageEncoder();
        commandSort();
    }

    private void commandSort() {
        if (decoder.getCommand(this.message) == DOWNLOAD_FILE_FROM_SERVER) {
            sendFileToClient();
        }
    }

    private void sendFileToClient() {
        File file = new File(SERVER_DIRECTORY + "/" + decoder.getFileName(this.message));
        ServerUtilities.sendMessageToClient(ctx, encoder.getMessage(file));
    }
}
