package com.easyrpc.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * 框架使用的属性操作工具类
 * <p>需要在classpath下加入easyRpc.properties</p>
 *
 * @author: guanjie
 */
public final class EasyRpcPropertiesUtil {

    static Properties properties;

    static {
        try {
            properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("easyRpc.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private EasyRpcPropertiesUtil() {
    }

    public static String getString(String key) {
        return properties.getProperty(key);
    }

    public static Integer getInt(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }
}
