package com.easyrpc.spring;

import com.easyrpc.client.ServerClient;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态生成代理类
 *
 * @author: guanjie
 */
public class ServerProxy implements InvocationHandler {

    private Field field;

    public ServerProxy(Field field) {
        this.field = field;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class[] types = null;
        if (args != null) {
            types = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                types[i] = args[i].getClass();
            }
        }
        Reference reference = AnnotationUtils.findAnnotation(field, Reference.class);
        return ServerClient.invoke(reference.contract().getName(), reference.implCode(), method.getName(), args, types);
    }


    public static <T> T getProxy(Field field) {
        return (T) Proxy.newProxyInstance(ServerProxy.class.getClassLoader(), new Class[]{field.getType()}, new ServerProxy(field));
    }
}
