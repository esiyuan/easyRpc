package com.easyrpc.spring;

import java.lang.annotation.*;

/**
 * Rpc动态属性注入注解
 * @author: guanjie
 */
@Target(value = {ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Reference {

    /**
     * 契约接口类
     *
     * @return 契约接口类
     */
    Class<?> contract();


    /**
     * implCode
     *
     * @return implCode
     */
    String implCode() default "";
}
