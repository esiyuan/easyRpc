package com.easyrpc.protocol;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @desc :
 * @author: guanjie
 */
@Setter
@Getter
@ToString
public class Request implements Serializable {

    private String id;
    private String contract;
    private String implCode;
    private String method;
    private Object[] args;
    private Class<?>[] parameterTypes;

}
