package com.easyrpc.client;

import com.easyrpc.protocol.InvocationMsg;
import com.easyrpc.protocol.MessageDecoder;
import com.easyrpc.protocol.MessageEncoder;
import com.easyrpc.protocol.Request;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
@Slf4j
public class NettyChannel {

    private String host;
    private Integer port;
    private volatile Channel channel;

    public NettyChannel(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public void send(Object msg) {
        if (channel == null || !channel.isActive()) {
            synchronized (this) {
                if (channel == null || !channel.isActive()) {
                    try {
                        channel = connect(host, port).sync().channel();
                    } catch (InterruptedException e) {
                        throw new ConnectExcetion(e);
                    }
                }
            }
        }
        channel.writeAndFlush(msg);
    }



    private ChannelFuture connect(String host, int port) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup(1));
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, false);
        bootstrap.option(ChannelOption.SO_REUSEADDR, true);
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new IdleStateHandler(0, 30, 0, TimeUnit.SECONDS), new MessageEncoder(), new MessageDecoder(), new ClientMsgHandler());
            }
        });
        return bootstrap.connect(host, port);
    }
}
