package com.zhx.baselibrary.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义Click实践注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventType(listenerType = View.OnClickListener.class, listenerSetter = "setOnClickListener")
public @interface OnClick {
    /**
     * 定义id数组，可以绑定多个view
     *
     * @return
     */
    int[] value() default {View.NO_ID};

}
