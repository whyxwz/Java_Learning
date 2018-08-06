package com.test;

import java.math.BigDecimal;

/**
 * Created by xwz on 2018/3/2.
 */
public class Test1 {

    public static void main(String[] args){

        System.out.println(Thread.currentThread().getName());
        String str = "helloworld";
        System.out.println(str);

        BigDecimal big = new BigDecimal("0.00000001");
        System.out.println(big.stripTrailingZeros());

        long l = 123232L;
        double d = 123232.01;
        if(l > d){
            System.out.println("11111");
        }else{
            System.out.println("222222");
        }



    }

}
