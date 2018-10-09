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
public class Response implements Serializable {

    private String id;
    private String resp;
}
