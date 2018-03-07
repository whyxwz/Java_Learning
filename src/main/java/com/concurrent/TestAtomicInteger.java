package com.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

public class TestAtomicInteger {

    static AtomicInteger atoInt = new AtomicInteger();
    static Integer i = new Integer(0);

    public static class AddThread implements Runnable {
        public void run() {
            for (int k = 0; k < 100; k++) {
                atoInt.incrementAndGet();
                i = i + 1;
            }
        }
    }

    public static void main(String[] args) {

        Thread[] thread = new Thread[10];

        for (int i = 0; i < 10; i++) {
            thread[i] = new Thread(new AddThread());
        }

        for (int i = 0; i < 10; i++) {
            thread[i].start();
        }

        for (int i = 0; i < 10; i++) {
            try {
                thread[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(atoInt);
        System.out.println(i);
    }

}
