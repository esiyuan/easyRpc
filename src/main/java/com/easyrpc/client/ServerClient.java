package com.easyrpc.client;

import com.easyrpc.protocol.InvocationMsg;
import com.easyrpc.protocol.Request;
import com.easyrpc.register.ZookeeperCoordinator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务远程调用的客户端
 *
 * @author: guanjie
 */
@Slf4j
public class ServerClient {

    private static final ConcurrentHashMap<String, NettyChannel> IP_CHANNEL_MAP = new ConcurrentHashMap<>();

    public static Object invoke(String contract, String implCode, String method, Object[] args, Class<?>[] parameterTypes) {
        Request request = new Request();
        request.setArgs(args);
        request.setContract(contract);
        request.setImplCode(implCode);
        request.setMethod(method);
        request.setParameterTypes(parameterTypes);
        request.setId(UUID.randomUUID().toString());
        String path = "/" + contract + "/" + implCode;
        List<String> list = ZookeeperCoordinator.getInstance().getChildrenList(path);
        String server = list.get(RandomUtils.nextInt(0, list.size()));
        return send(server, request).get().toResponse().getResp();
    }


    private static Future<InvocationMsg> send(String server, Request request) {
        log.info("发送消息：{}", request);
        InvocationMsg invocationMsg = InvocationMsg.from(request);
        InvocationMsgFuture invocationMsgFuture = new InvocationMsgFuture();
        MsgFutreManager.save(request.getId(), invocationMsgFuture);
        getChannel(server).send(invocationMsg);
        return invocationMsgFuture;
    }

    private static NettyChannel getChannel(String server) {
        NettyChannel nettyChannel;
        if ((nettyChannel = IP_CHANNEL_MAP.get(server)) == null) {
            synchronized (IP_CHANNEL_MAP) {
                if ((nettyChannel = IP_CHANNEL_MAP.get(server)) == null) {
                    String[] ipAndPort = StringUtils.split(server, "@");
                    nettyChannel = new NettyChannel(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
                    IP_CHANNEL_MAP.put(server, nettyChannel);
                }
            }
        }
        return nettyChannel;
    }


}
