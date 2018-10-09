package com.easyrpc.client;

import com.easyrpc.protocol.InvocationMsg;

/**
 * @desc :
 * @author: guanjie
 */
public class InvocationMsgFuture implements Future<InvocationMsg> {


    private InvocationMsg invocationMsg;

    @Override
    public void set(InvocationMsg InvocationMsg) {
        synchronized (this) {
            this.invocationMsg = InvocationMsg;
            notifyAll();
        }
    }

    @Override
    public boolean isDone() {
        return invocationMsg == null;
    }

    @Override
    public InvocationMsg get() {
        synchronized (this) {
            while (invocationMsg == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return invocationMsg;
    }


}
