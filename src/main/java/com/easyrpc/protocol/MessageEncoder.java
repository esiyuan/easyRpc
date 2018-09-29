package com.easyrpc.protocol;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @desc :
 * @author: guanjie
 */
@Slf4j
public class MessageEncoder extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        byte[] msgBytes = JSON.toJSONString(msg).getBytes(Charsets.UTF_8);
        out.writeInt(msgBytes.length);
        out.writeBytes(msgBytes);
    }
}
