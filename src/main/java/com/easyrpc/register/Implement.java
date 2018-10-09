package com.easyrpc.register;

import java.lang.annotation.*;

/**
 * 服务提供的标准注解
 * @author: guanjie
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Implement {

    Class<?> contract();

    String implCode() default "";
}
