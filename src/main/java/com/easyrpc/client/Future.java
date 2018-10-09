package com.easyrpc.client;

/**
 * @desc :
 * @author: guanjie
 */
public interface Future<T> {

    boolean isDone();

    T get();


    void set(T t);

}
