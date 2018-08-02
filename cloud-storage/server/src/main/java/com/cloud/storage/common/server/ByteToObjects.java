package com.cloud.storage.common.server;

import com.cloud.storage.common.ServiceCommands;
import com.cloud.storage.common.message.AuthMessage;
import com.cloud.storage.common.message.FileMessage;
import com.cloud.storage.common.message.RegistrationMessage;
import com.cloud.storage.common.message.CommandMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ByteToObjects extends ByteToMessageDecoder implements ServiceCommands {
    int size = 0;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> list) throws Exception {

        if (in.readByte() == FILE_SIZE_SEPARATE) {
            byte[] sizeBytes = new byte[in.readerIndex() - 1];
            in.resetReaderIndex();
            in.readBytes(sizeBytes, 0, sizeBytes.length);
            size = Integer.valueOf(new String(sizeBytes));
        }

        if (size > 0) {
            if (in.readableBytes() < size) {
                return;
            }
            in.readByte();

            byte[] bytes = new byte[size];
            in.readBytes(bytes);
            switch (bytes[0]) {
                case FILE_CODE:
                    list.add(new FileMessage(bytes).getMessageObject());
                    break;
                case COMMAND_CODE:
                    list.add(new CommandMessage(bytes).getMessageObject());
                    break;
                case AUTHORIZATION_CODE:
                    list.add(new AuthMessage(bytes).getMessageObject());
                    break;
                case REGISTRATION_CODE:
                    list.add(new RegistrationMessage(bytes).getMessageObject());
                    break;
            }
            size = 0;
        }
    }
}

