package com.easyrpc.server;

import com.alibaba.fastjson.JSON;
import com.easyrpc.protocol.InvocationMsg;
import com.easyrpc.protocol.Response;
import com.easyrpc.register.ImplementKey;
import com.easyrpc.register.ServerRegister;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.MethodUtils;

/**
 * 通过消息调用对应的方法
 *
 * @author: guanjie
 */
@Slf4j
public class ServerMsgHandler extends SimpleChannelInboundHandler<InvocationMsg> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, InvocationMsg msg) throws Exception {
        log.info("request = {}", msg);
        ImplementKey implementKey = new ImplementKey(msg.getContract(), msg.getImplCode());
        Object bean = ServerRegister.getBy(implementKey);
        Object result = MethodUtils.invokeMethod(bean, msg.getMethod(), msg.getArgs(), msg.getParameterTypes());
        Response response = new Response();
        response.setId(msg.getId());
        response.setResp(JSON.toJSONString(result));
        ctx.writeAndFlush(InvocationMsg.from(response));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("", cause);
    }
}
