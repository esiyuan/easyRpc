package com.easyrpc.util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * RPC框架使用线程池工具类
 *
 * @author: guanjie
 */
public class EasyRpcExecutorUtil {


    private static Executor executor = Executors.newFixedThreadPool(10);


    public static void execute(Runnable runnable) {
        executor.execute(runnable);
    }


}
