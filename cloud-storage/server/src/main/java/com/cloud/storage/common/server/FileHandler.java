package com.cloud.storage.common.server;

import com.cloud.storage.common.Directorys;
import com.cloud.storage.common.MessageEncoder;
import com.cloud.storage.common.ServiceCommands;
import com.cloud.storage.common.message.FileMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;

public class FileHandler implements Directorys, ServiceCommands {

    public static final Logger logger = LogManager.getLogger(FileHandler.class.getName());

    private ChannelHandlerContext ctx;
    private FileMessage msg;
    private User user;
    MessageEncoder encoder;

    public FileHandler(ChannelHandlerContext ctx, Object msg, User user) {
        this.user = user;
        this.ctx = ctx;
        this.msg = (FileMessage) msg;
        encoder = new MessageEncoder();
        if (msg != null) {
            createFile(this.msg);
        }
    }

    private void createFile(FileMessage message) {
        MessageDecoder decoder = new MessageDecoder();
        MyFile myFile = null;
        try {
            myFile = decoder.getFile(message);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ServerUtilities.sendMessageToClient(ctx, encoder.getMessage(ERROR));
            return;
        }
        int userId = user.getId();
        if (userId != -1) {
            if (myFile.addFile(userId)) {
                ServerUtilities.sendMessageToClient(ctx, encoder.getFileListMessage(ServerUtilities.getFileList()));
                logger.info("User: " + user.getId() + ". " + user.getNickName() + "send file: " + myFile.getFileName());
            } else {
                ServerUtilities.sendMessageToClient(ctx, encoder.getMessage(FILE_EXIST));
            }
        } else {
            ServerUtilities.sendMessageToClient(ctx, encoder.getMessage(ERROR));
        }
    }
}
