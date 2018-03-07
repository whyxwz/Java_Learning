package com.redis;

/**
 * Created by xwz on 2018/3/7.
 */
public class ThreadA extends Thread{
    private Service service;

    public ThreadA(Service service) {
        this.service = service;
    }

    @Override
    public void run() {
        service.seckill();
    }
}
