package com.easyrpc.register;

import com.easyrpc.server.NettyServer;
import com.easyrpc.server.ServerMsgHandler;
import com.easyrpc.util.EasyRpcExecutorUtil;
import com.easyrpc.util.EasyRpcPropertiesUtil;
import com.easyrpc.util.IpOrPidUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RPC服务方注册
 *
 * @author: guanjie
 */
@Component
@Slf4j
public class ServerRegister implements ApplicationListener<ContextRefreshedEvent> {

    private int port = EasyRpcPropertiesUtil.getInt("rpc.port");

    private ZookeeperCoordinator zookeeperCoordinator = ZookeeperCoordinator.getInstance();

    private static Map<ImplementKey, Object> IMPLEMENT_CACHE = new ConcurrentHashMap<>();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        final String localIp = IpOrPidUtil.getLocalIp();
        int realPort = NettyServer.init(localIp, port, new ServerMsgHandler());
        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(Implement.class);
        for (Object bean : beanMap.values()) {
            Implement implement = AnnotationUtils.getAnnotation(AopProxyUtils.ultimateTargetClass(bean), Implement.class);
            IMPLEMENT_CACHE.put(new ImplementKey(implement.contract().getName(), implement.implCode()), bean);
            register(implement, localIp, realPort);
        }

    }


    private void register(Implement implement, String ip, int port) {
        String path = "/" + implement.contract().getName() + "/" + implement.implCode() + "/" + ip + "@" + port;
        zookeeperCoordinator.persistNode(path, true);
    }

    public static Object getBy(ImplementKey implementKey) {
        return IMPLEMENT_CACHE.get(implementKey);
    }


}
