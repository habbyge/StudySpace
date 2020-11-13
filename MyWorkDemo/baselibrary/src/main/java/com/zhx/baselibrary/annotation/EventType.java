package com.zhx.baselibrary.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 事件注解的注解
 */

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventType {

    Class listenerType();

    /**
     * 用以接收方法名，供动态代理使用
     * @return
     */
    String listenerSetter() default "";

}
