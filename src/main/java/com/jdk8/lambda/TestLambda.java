package com.jdk8.lambda;

/**
 * Created by xwz on 2018/7/1.
 */
public class TestLambda {

    public static void main(String[] args) {
        //String a = "abc";
        TestLambdaInterface testLambda = (a) -> System.out.println(a);

        String a = "abc";
        testLambda.test(a);

    }
}
