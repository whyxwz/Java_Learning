package com.dynamicProxy;

/**
 * Created by xwz on 2018/4/11.
 */
public class RealSubject implements Subject {

    @Override
    public void sayHello(String name) {
        System.out.println("hello " + name);
    }
}
