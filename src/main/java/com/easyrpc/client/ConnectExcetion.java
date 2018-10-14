package com.easyrpc.client;

public class ConnectExcetion extends RuntimeException {

    public ConnectExcetion() {
        super();
    }

    public ConnectExcetion(String message) {
        super(message);
    }

    public ConnectExcetion(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectExcetion(Throwable cause) {
        super(cause);
    }

    protected ConnectExcetion(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
