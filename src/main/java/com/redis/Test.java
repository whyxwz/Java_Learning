package com.redis;

/**
 * Created by xwz on 2018/3/7.
 */
public class Test {
    public static void main(String[] args) {
        Service service = new Service();
        for (int i = 0; i < 200; i++) {
            ThreadA threadA = new ThreadA(service);
            threadA.start();
/*            try {
                Thread.currentThread().sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }
}
