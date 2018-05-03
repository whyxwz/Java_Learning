package com.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by xwz on 2018/4/11.
 */
public class TestInvocateHandler implements InvocationHandler {

    private Object target;

    public TestInvocateHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before-1");
        Object result = method.invoke(target, args);
        System.out.println("after-2");
        return result;
    }
}
