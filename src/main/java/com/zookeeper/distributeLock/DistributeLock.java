package com.zookeeper.distributeLock;

import com.alibaba.fastjson.JSON;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by lucas on 2018/5/18.
 */
public class DistributeLock implements Watcher{

    static int n = 500;
    private ZooKeeper zk = null;
    private CountDownLatch countDownLatch;
    private static String HOST_NAME = "172.25.1.1:2181,172.25.1.101:2181,172.25.1.174:2181";
    private static int SESSION_TIMEOUT = 10000;
    private static String ROOT_PATH = "/lock";
    private static String SPLIT_STR = "_split_";
    private String WAIT_LOCK;
    private String CURRENT_LOCK;

    DistributeLock() throws IOException, InterruptedException, KeeperException {
        //创建zk连接的session
        zk = new ZooKeeper(HOST_NAME, SESSION_TIMEOUT, this);

        //创建分布式锁的根节点
        Stat stat = zk.exists(ROOT_PATH, false);
        if(stat == null){
            zk.create(ROOT_PATH,  new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
    }

    //获取锁
    public void lock(String lockName) throws KeeperException, InterruptedException {
        if(tryLock(lockName)){
            System.out.println(Thread.currentThread().getName() + ": get Lock");
        }else{
            lockWait(WAIT_LOCK, 1000L);
        }
    }

    public boolean tryLock(String lockName) throws KeeperException, InterruptedException {
        boolean result = false;
        //尝试建立临时顺序节点
        CURRENT_LOCK = zk.create(ROOT_PATH  + "/" + lockName + SPLIT_STR,
                new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        //获取ROOT_LOCK节点下所有的字节点
        List<String> subNodes = zk.getChildren(ROOT_PATH, false);
        List<String> curLockSubNodes = new ArrayList<String>();
        for(String node : subNodes){
            if(node.contains(lockName)){
                curLockSubNodes.add(node);
            }
        }
        //对临时顺序节点排序
        Collections.sort(curLockSubNodes);
        if(CURRENT_LOCK.contains(curLockSubNodes.get(0))){
            //当前线程获取到锁
            result = true;
        }else{
            //找当前节点的前一个节点，并注册对该节点的监听
            String sub = CURRENT_LOCK.substring(CURRENT_LOCK.lastIndexOf("/") + 1);
            WAIT_LOCK = curLockSubNodes.get(Collections.binarySearch(curLockSubNodes, sub)-1);
            System.out.println("CUR = " + CURRENT_LOCK + ", WAIT_LOCK = " + WAIT_LOCK);
        }
        return result;
    }

    public void lockWait(String prevNode, long timeOut) throws KeeperException, InterruptedException {
        //注册对当前获取锁的节点的监听
        Stat stat = zk.exists(ROOT_PATH + "/" + prevNode, true);

        if(stat != null){
            System.out.println(Thread.currentThread().getName() + "等待锁 " + ROOT_PATH + "/" + prevNode);
            countDownLatch = new CountDownLatch(1);
            countDownLatch.await(timeOut, TimeUnit.MICROSECONDS);
            countDownLatch = null;
            System.out.println(Thread.currentThread().getName() + " 等到了锁");
        }

    }

    public void unlock(){
        try {
            System.out.println(Thread.currentThread().getName() + "释放锁 " + CURRENT_LOCK);
            zk.delete(CURRENT_LOCK, -1);
            CURRENT_LOCK = null;
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void process(WatchedEvent event) {
        //System.out.println("recieve watch event :" + event);
        if(countDownLatch != null){
            countDownLatch.countDown();
            System.out.println(Thread.currentThread().getName() + ",WAIT_LOCK = " + WAIT_LOCK);
        }
    }

    public static void secskill() {
        System.out.println(--n);
    }

    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        Runnable runnable = new Runnable() {
            public void run() {
                DistributeLock lock = null;
                try {
                    lock = new DistributeLock();
                    lock.lock("testLock");
                    System.out.println(Thread.currentThread().getName() + "正在运行" + lock.CURRENT_LOCK);
                    secskill();

                } catch(Exception e){
                    e.printStackTrace();
                }finally {
                    if (lock != null) {
                        lock.unlock();
                    }
                }
            }
        };

        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(runnable);
            t.start();
        }

    }

}
