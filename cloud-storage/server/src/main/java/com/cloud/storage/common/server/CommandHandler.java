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
        System.out.println(encoder.getMessage(file)[encoder.getMessage(file).length - 15]);
        ctx.write(encoder.getMessage(file));
        ctx.flush();
    }

    public static void print(byte[] arr, int size) {
        for (int i = 0; i < size; i++) {
            System.out.print(" " + arr[i]);
            if (i % 15 == 0) System.out.println();
        }
        // System.out.println();
    }
}
