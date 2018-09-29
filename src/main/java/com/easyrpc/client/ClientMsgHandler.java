package com.easyrpc.client;

import com.easyrpc.protocol.InvocationMsg;
import com.easyrpc.protocol.Request;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @desc :
 * @author: guanjie
 */
@Slf4j
@ChannelHandler.Sharable
public class ClientMsgHandler extends SimpleChannelInboundHandler<InvocationMsg> {

    private volatile Channel channel;

    private static ConcurrentHashMap<String, Future<InvocationMsg>> INVOCATION_MSG_MAP = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, InvocationMsg msg) throws Exception {
        log.info("消息接收 : {}", msg);
        INVOCATION_MSG_MAP.remove(msg.getId()).set(msg);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        channel = ctx.channel();
    }


    public Future<InvocationMsg> send(Request request) {
        log.info("消息发送 : {}", request);
        InvocationMsg invocationMsg = InvocationMsg.from(request);
        InvocationMsgFuture invocationMsgFuture = new InvocationMsgFuture();
        INVOCATION_MSG_MAP.put(request.getId(), invocationMsgFuture);
        channel.writeAndFlush(invocationMsg);
        return invocationMsgFuture;
    }

    public void close() {
        channel.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("", cause);
    }
}
