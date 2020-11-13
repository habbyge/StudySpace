package com.zhx.javalib;

import com.zhx.javalib.generic.Apple;
import com.zhx.javalib.generic.AppleBox;
import com.zhx.javalib.generic.Banana;
import com.zhx.javalib.generic.Box;
import com.zhx.javalib.generic.ColorBox;
import com.zhx.javalib.generic.Fruit;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 用于学习Java核心技术
 */
public class MyJavaModule {
    public static void main(String[] args) {
        System.out.println("hello java module");
//        Object[] objects = new Object[3];
//        String[] strings = new String[]{"a","b","c"};
//        objects = strings;
//        objects[1] = new Box<>();
//        System.out.println(strings instanceof Object[]);
//        System.out.println("objects -> " + objects[0]);

        Box<? extends Object>[] boxes = new Box<?>[5];
        boxes[0] = new Box<Apple>();
        boxes[1] = new Box<Banana>();
//        sort(boxes);
//        List<String> listString = Arrays.asList(strings);
//        ArrayList<Object> listInteger = new ArrayList<>();
//        sort(listString);
//        listInteger.add("sss");
//        System.out.println("listInteger -> " + listInteger.getClass());
//        System.out.println("listString -> " + listString.getClass());

//        Box<Apple> appleBox = new Box<>();
//        Box<Banana> bananaBox = new Box<>();
//        System.out.println("bananaBox -> " + appleBox.getClass());
//        System.out.println("appleBox -> " + bananaBox.getClass());

//        Apple[] apples = new Apple[10];
//        Fruit[] fruits = new Fruit[10];
//        System.out.println(apples instanceof Fruit[]);


//        Box<?>[] appleBox = new Box<?>[3];
//
//        Box<Fruit> fruitBox = new Box<>();
//        fruitBox.setT(new Banana());
//
//        Box<Apple> appleBox1 = new Box<>();
//        appleBox1.setT(new Apple());
//
//        ColorBox<Apple> colorBox = new ColorBox<>();
//        appleBox1 = colorBox;

//        Field field = null;
//        try {
//            field = fruitBox.getClass().getDeclaredField("t");
//            if (field.getGenericType() instanceof ParameterizedType){
//                ParameterizedType pType =  (ParameterizedType)field.getGenericType();
//                System.out.println(" getRawType = "+pType.getRawType());
//                System.out.println(" getActualTypeArguments = "+pType.getActualTypeArguments()[0]);
//                System.out.println(" getOwnerType = "+pType.getOwnerType());
//            }else{
//                System.out.println(" getType = "+field.getType());
//            }
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }

        new MyThread1().start();
    }

    /**
     * 成员
     */
    Box<Fruit> fruitBox;

    public static class MyThread1 extends Thread{
        @Override
        public void run() {
            while(true){

            }
        }
    }
}