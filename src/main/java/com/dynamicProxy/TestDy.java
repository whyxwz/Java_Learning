package com.dynamicProxy;

import com.alibaba.fastjson.JSON;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Proxy;

/**
 * Created by xwz on 2018/4/11.
 */
public class TestDy {

    public static void main(String[] args) {

        RealSubject realSubject = new RealSubject();

        ClassLoader loader = RealSubject.class.getClassLoader();
        Class<?>[] classes = new Class[]{Subject.class};
        TestInvocateHandler handler = new TestInvocateHandler(realSubject);

        Object obejct = Proxy.newProxyInstance(loader, classes, handler);

        System.out.println(JSON.toJSONString(obejct.getClass().getMethods()));

        Subject subject = (Subject) obejct;
        System.out.println(subject.getClass());
        subject.sayHello("abcd");


    }

}
