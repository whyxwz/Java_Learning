package com.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xwz on 2018/7/29.
 */
public class TestThreadLocal {

    private static final AtomicInteger autoInt = new AtomicInteger(0);

    private static final ThreadLocal<Integer> uniqueInt = new ThreadLocal<Integer>(){
        @Override
        protected Integer initialValue() {
            return autoInt.getAndIncrement();
        }
    };

    public static void main(String[] args){

        Thread[] threads = new Thread[5];
        for (int i = 0 ; i < 5; i++ ){
            String name = "Thread-" + i;
            threads[i] = new Thread(name){
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + ": " + uniqueInt.get());
                }
            };
            threads[i].start();
        }
        System.out.println(Thread.currentThread().getName() + ": " + uniqueInt.get());
    }



}
