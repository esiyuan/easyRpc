package com.easyrpc.spring;

import com.easyrpc.client.ServerClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

/**
 * 处理SpringBean的{@link Reference} 属性的注入
 *
 * @author: guanjie
 */
@Component
@Slf4j
public class ReferenceBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

    @Autowired
    private ServerClient serverClient;

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, final Object bean, final String beanName) throws BeansException {
        ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                FieldUtils.writeField(field, bean, ServerProxy.getProxy(field, serverClient), true);
            }
        }, new ReflectionUtils.FieldFilter() {
            @Override
            public boolean matches(Field field) {
                return field.isAnnotationPresent(Reference.class);
            }
        });
        return pvs;
    }
}
