package com.easyrpc.protocol;

import com.alibaba.fastjson.JSON;
import com.easyrpc.util.EasyRpcConstants;
import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @desc :
 * @author: guanjie
 */
@Slf4j
public class MessageDecoder extends LengthFieldBasedFrameDecoder {

    public MessageDecoder() {
        super(Integer.MAX_VALUE, 0, EasyRpcConstants.MSG_LENGTH, 0, EasyRpcConstants.MSG_LENGTH);
    }


    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null) {
            return null;
        }
        InvocationMsg invocationMsg = JSON.parseObject(frame.toString(Charsets.UTF_8), InvocationMsg.class);
        return invocationMsg;
    }
}
