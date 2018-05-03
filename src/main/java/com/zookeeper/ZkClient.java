package com.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by xwz on 2018/4/17.
 */
public class ZkClient implements Watcher{

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws IOException {

        String zkhost = "172.25.1.1:2181,172.25.1.101:2181,172.25.1.174:2181";
        int sessionTimeout = 1000;
        ZooKeeper zooKeeper = new ZooKeeper(zkhost, sessionTimeout, new ZkClient());

        System.out.println(zooKeeper.getState());

        try {
            connectedSemaphore.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("session established");


    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("recieve watch event :" + event);
        if(Event.KeeperState.SyncConnected.equals(event.getState())){
            connectedSemaphore.countDown();
        }
    }
}
