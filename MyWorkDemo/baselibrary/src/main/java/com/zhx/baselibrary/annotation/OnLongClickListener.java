package com.zhx.baselibrary.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventType(listenerType = View.OnLongClickListener.class, listenerSetter = "setOnLongClickListener")
public @interface OnLongClickListener {

    int[] value() default {View.NO_ID};

}
