package com.cloud.storage.common.server;

import com.cloud.storage.common.ServiceCommands;
import com.cloud.storage.common.server.DataBase.DBHelper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ServerMainClass implements ServiceCommands {
    public void run() throws Exception {
        DBHelper.getInstance().connect();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                   .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ToByteBufHandler(), new ByteToObjects(), new AuthorizationHandler());
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(SERVER_PORT).sync();

            f.channel().closeFuture().sync();
        } finally {
            DBHelper.getInstance().disconnectDb();
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new ServerMainClass().run();
    }
}
