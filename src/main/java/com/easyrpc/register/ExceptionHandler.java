package com.easyrpc.register;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;

/**
 * @desc :
 * @author: guanjie
 */
@Slf4j
public class ExceptionHandler {


    public static void zkExceptionHandler(Exception e) {
        if (e instanceof KeeperException.NodeExistsException) {
            log.debug("节点存在，忽略异常!");
        } else {
            log.error("", e);
        }
    }
}
