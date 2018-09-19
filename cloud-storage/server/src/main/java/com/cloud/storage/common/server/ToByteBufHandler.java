package com.cloud.storage.common.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.util.Arrays;

public class ToByteBufHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        byte[] arr = (byte[])msg;
        ByteBufAllocator al = new PooledByteBufAllocator();
        ByteBuf buf = al.buffer(arr.length);
        buf.writeBytes(arr);
        ctx.write(buf);
    }
}
