package com.easyrpc.server;

import com.easyrpc.protocol.MessageDecoder;
import com.easyrpc.protocol.MessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.BindException;

/**
 * netty 服务端启动引导类
 *
 * @author: guanjie
 */
public class NettyServer {


    public static int init(String host, int port, final ChannelInboundHandler channelInboundHandler) {
        EventLoopGroup workerGroup = new NioEventLoopGroup(0);
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        ServerBootstrap s = new ServerBootstrap();
        s.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new MessageEncoder(), new MessageDecoder(), channelInboundHandler);
                    }
                });
        while (true) {
            try {
                s.bind(host, port).sync();
                break;
            } catch (Exception e) {
                if (e instanceof BindException) {
                    port++;
                } else {
                    throw new RuntimeException(e);
                }
            }
        }
        return port;
    }
}
