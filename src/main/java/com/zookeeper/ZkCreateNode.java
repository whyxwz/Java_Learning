package com.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by xwz on 2018/5/7.
 */
public class ZkCreateNode {

    public static void main(String[] args) {

        String zkhost = "172.25.1.1:2181,172.25.1.101:2181,172.25.1.174:2181";
        int sessionTimeout = 1000;
        try {
            ZooKeeper zooKeeper = new ZooKeeper(zkhost, sessionTimeout, new ZkClient());
            System.out.println(zooKeeper.getState());
            try{
                ZkClient.connectedSemaphore.await();
            }catch (Exception e){
                e.printStackTrace();
            }
            System.out.println("session established");

            String path = zooKeeper.create("/testZk1", "testZk1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT );

            System.out.println("node created : " + path);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
