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
public class ClientMsgHandler extends SimpleChannelInboundHandler<InvocationMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, InvocationMsg msg) throws Exception {
        log.info("消息接收 : {}", msg);
        MsgFutreManager.getAndRemove(msg.getId()).set(msg);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("", cause);
    }
}
