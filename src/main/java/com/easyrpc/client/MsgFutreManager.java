package com.easyrpc.client;

import com.easyrpc.protocol.InvocationMsg;

import java.util.concurrent.ConcurrentHashMap;

public class MsgFutreManager {

    private static ConcurrentHashMap<String, Future<InvocationMsg>> INVOCATION_MSG_MAP = new ConcurrentHashMap<>();


    public static void save(String id, Future<InvocationMsg> future) {
        INVOCATION_MSG_MAP.putIfAbsent(id, future);
    }

    public static Future<InvocationMsg> getAndRemove(String id) {
        return INVOCATION_MSG_MAP.remove(id);
    }

}
