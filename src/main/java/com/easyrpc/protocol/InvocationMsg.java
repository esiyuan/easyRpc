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
public class InvocationMsg implements Serializable {

    private String id;
    private String contract;
    private String implCode;
    private String method;
    private Object[] args;
    private Class<?>[] parameterTypes;

    private Boolean request;
    private String resp;


    public static InvocationMsg from(Request request) {
        InvocationMsg invocationMsg = new InvocationMsg();
        invocationMsg.id = request.getId();
        invocationMsg.contract = request.getContract();
        invocationMsg.implCode = request.getImplCode();
        invocationMsg.args = request.getArgs();
        invocationMsg.method = request.getMethod();
        invocationMsg.parameterTypes = request.getParameterTypes();
        invocationMsg.request = true;
        return invocationMsg;
    }


    public static InvocationMsg from(Response response) {
        InvocationMsg invocationMsg = new InvocationMsg();
        invocationMsg.setId(response.getId());
        invocationMsg.setResp(response.getResp());
        invocationMsg.setRequest(false);

        return invocationMsg;
    }

    public Response toResponse() {
        Response response = new Response();
        response.setId(id);
        response.setResp(resp);
        return response;
    }
}
