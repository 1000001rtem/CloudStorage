package com.cloud.storage.common.server;

import com.cloud.storage.common.Directorys;
import com.cloud.storage.common.MessageEncoder;
import com.cloud.storage.common.ServiceCommands;
import com.cloud.storage.common.message.FileMessage;
import io.netty.channel.ChannelHandlerContext;

public class FileHandler implements Directorys, ServiceCommands {

    private ChannelHandlerContext ctx;
    private FileMessage msg;
    private User user;
    MessageEncoder encoder;

    public FileHandler(ChannelHandlerContext ctx, Object msg, User user) {
        this.user = user;
        this.ctx = ctx;
        this.msg = (FileMessage) msg;
        encoder = new MessageEncoder();
        if(msg != null){
            createFile(this.msg);
        }
    }

    private void createFile(FileMessage message) {
        MessageDecoder decoder = new MessageDecoder();
        MyFile myFile = decoder.getFile(message);
        if (myFile.addFile(user.getId())) {
            ctx.write(encoder.getFileListMessage(ServerUtilities.getFileList()));
            ctx.flush();
        } else{
            ctx.write(encoder.getMessage(FILE_EXIST));
            ctx.flush();
        }
    }
}
