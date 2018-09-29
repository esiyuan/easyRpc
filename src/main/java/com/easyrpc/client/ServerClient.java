package com.easyrpc.client;

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

    private static ConcurrentHashMap<String, ClientMsgHandler> IP_MSG_MAP = new ConcurrentHashMap<>();

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
        ClientMsgHandler clientMsgHandler = null;
        if ((clientMsgHandler = IP_MSG_MAP.get(server)) == null) {
            synchronized (IP_MSG_MAP) {
                if ((clientMsgHandler = IP_MSG_MAP.get(server)) == null) {
                    String[] ipAndPort = StringUtils.split(server, "@");
                    clientMsgHandler = NettyClient.init(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
                    IP_MSG_MAP.put(server, clientMsgHandler);
                }
            }
        }
        return clientMsgHandler.send(request).get().toResponse().getResp();
    }


    private void listener(String path) {
        ZookeeperCoordinator.getInstance().listenPath(path, new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                    event.getData().getPath();
            }
        });
    }

}
