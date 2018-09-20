package com.cloud.storage.common.server;

import com.cloud.storage.common.Directorys;
import com.cloud.storage.common.MessageEncoder;
import com.cloud.storage.common.ServiceCommands;
import com.cloud.storage.common.message.CommandMessage;
import com.cloud.storage.common.server.DataBase.FileTable;
import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        else if(decoder.getCommand(this.message)==DELETE_FILE_FROM_SERVER){
            deleteFile(decoder.getFilePath(this.message));
        }
    }

    private void deleteFile(String filePath) {
        FileTable table = new FileTable();
        if(table.deleteFile(filePath)) {
            try {
                Files.delete(Paths.get(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            ServerUtilities.sendMessageToClient(ctx, encoder.getFileListMessage(ServerUtilities.getFileList()));
        }else {
            ServerUtilities.sendMessageToClient(ctx, encoder.getMessage(ERROR));
        }
    }

    private void sendFileToClient() {
        File file = new File(decoder.getFilePath(this.message));
        ServerUtilities.sendMessageToClient(ctx, encoder.getMessage(file));
    }
}
