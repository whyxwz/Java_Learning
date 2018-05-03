package com.dynamicProxy;

import java.lang.reflect.Proxy;

/**
 * Created by xwz on 2018/4/11.
 */
public class TestDy {

    public static void main(String[] args) {

        RealSubject realSubject = new RealSubject();

        ClassLoader loader = RealSubject.class.getClassLoader();
        //Class<?>[] classes = RealSubject.class.getInterfaces();
        Class<?>[] classes = new Class[]{Subject.class};
        TestInvocateHandler handler = new TestInvocateHandler(realSubject);

        Subject subject = (Subject) Proxy.newProxyInstance(loader, classes, handler);
       System.out.println(subject.getClass());
        subject.sayHello("abcd");

    }

}
