package com.zhx.baselibrary.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.IdRes;

import com.zhx.baselibrary.annotation.Autowired;
import com.zhx.baselibrary.annotation.EventType;
import com.zhx.baselibrary.annotation.InjectView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * 注解+反射实践
 */
public class InjectUtils {

    /**
     * view的注入
     * InjectView
     */
    public static void injectView(Activity activity) {
        Class<? extends Activity> cls = activity.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(InjectView.class)) {
                InjectView injectView = field.getAnnotation(InjectView.class);
                @IdRes int resId = injectView.value();
                View view = activity.findViewById(resId);
                field.setAccessible(true);
                try {
                    //反射赋值给activity的field，如果是静态的则set()方法第一个参数传null
                    field.set(activity, view);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 事件注入
     */
    public static void injectEvent(Activity activity) {

        Class<? extends Activity> cls = activity.getClass();
        //获得所有声明的方法
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            Logger.e("method :: " + method.getName());
            //获得所有方法上声明的注解
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                Logger.e("annotation :: " + annotation.toString());
                //获取注解类型
                Class<? extends Annotation> annotationClass = annotation.annotationType();
                Logger.e("annotationClass :: " + annotationClass.getSimpleName());
                if (annotationClass.isAnnotationPresent(EventType.class)) {
                    //获取到事件注解在定义时指定的对应事件的Listener设置方法
                    EventType eventType = annotationClass.getAnnotation(EventType.class);
                    //listener对应接口
                    Class listenerType = eventType.listenerType();
                    //设置listner的方法名
                    String listenerSetter = eventType.listenerSetter();

                    //不需要知道具体是OnClick还是OnLongClick，知道接口和方法名即可动态代理之
                    try {
                        //反射获取到需要绑定的 view ids
                        Method valueMethod = annotationClass.getDeclaredMethod("value");
                        int[] viewIds = (int[]) valueMethod.invoke(annotation);

                        method.setAccessible(true);
                        ListenerInvokeHandler<Activity> handler = new ListenerInvokeHandler<>(activity, method);
                        //创建接口的动态代理类对象，无需关注具体类型，代理方法invoke之后，activity中声明的method方法将被调用到，以执行业务逻辑
                        Object listenerProxy = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType}, handler);

                        for (int viewId : viewIds) {
                            View view = activity.findViewById(viewId);
                            //获取指定的方法
                            Method setter = view.getClass().getMethod(listenerSetter, listenerType);
                            //执行view.setXXXXListener方法，由动态代理对象invoke方法，进行传递调用
                            setter.invoke(view, listenerProxy);
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * intent参数注入
     */
    public static void injectAutowired(Activity activity) {
        Class<? extends Activity> cls = activity.getClass();

        //读取Intent
        Intent intent = activity.getIntent();
        Bundle extras = intent.getExtras();
        if (extras == null) {
            return;
        }
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Autowired.class)) {
                Autowired autowired = field.getAnnotation(Autowired.class);
                //是否有自定义字段名
                String key = TextUtils.isEmpty(autowired.value()) ? field.getName() : autowired.value();
                if (extras.containsKey(key)) {
                    Object obj = extras.get(key);
                    //对于Parcelable[]需要特殊处理
                    Class<?> componentType = field.getType().getComponentType();
                    if (field.getType().isArray() && Parcelable.class.isAssignableFrom(componentType)) {
                        //手动创建Object数组并拷贝，进行数据类型转换
                        Object[] objs = (Object[]) obj;
                        Object[] objects = Arrays.copyOf(objs, objs.length, (Class<? extends Object[]>) field.getType());
                        obj = objects;
                    }

                    field.setAccessible(true);
                    try {
                        field.set(activity,obj);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    static class ListenerInvokeHandler<T> implements InvocationHandler {

        private Method method;

        private T target;

        public ListenerInvokeHandler(T target, Method method) {
            this.target = target;
            this.method = method;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Logger.e("ListenerInvokeHandler invoke method = " + method.getName());
            Logger.e("ListenerInvokeHandler this.method = " + this.method.getName());
            return this.method.invoke(target, args);
        }
    }
}
