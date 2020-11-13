package com.zhx.javalib.generic;

import java.util.ArrayList;
import java.util.List;

/**
 * 容器
 * @param <T>
 */
public class Box<T extends Fruit>{

    T t;

    public Box(){

    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    void test(List<? super Fruit> list){

    }

   void test2(List<?> list){

   }
}
