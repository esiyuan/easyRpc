package com.easyrpc.client;

import com.easyrpc.protocol.MessageDecoder;
import com.easyrpc.protocol.MessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @desc :
 * @author: guanjie
 */
public class NettyClient {

    public static ClientMsgHandler init(String host, int port) {
        final ClientMsgHandler clientMsgHandler = new ClientMsgHandler();
        Bootstrap b = new Bootstrap();
        NioEventLoopGroup work = new NioEventLoopGroup();
        b.group(work).channel(NioSocketChannel.class);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new MessageEncoder(), new MessageDecoder(), clientMsgHandler);
            }
        });
        try {
            b.connect(host, port).sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return clientMsgHandler;

    }

}
