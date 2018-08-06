package com.zookeeper.distributeLock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by xwz on 2018/5/15.
 */
public class DistributeLock1 implements Watcher{

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    private ZooKeeper zk = null;
    private static String ZKHOST = "172.25.1.1:2181,172.25.1.101:2181,172.25.1.174:2181";
    private static int SESSIONTIMEOUT = 1000;
    private static String ROOT_LOCK_PATH = "/distributeLock/";
    private String lockName = null;
    private String WAIT_LOCK = null;
    private String CURRENT_LOCK = null;

    public void connect(String lockName) throws IOException, KeeperException, InterruptedException {
        this.lockName = lockName;
        zk = new ZooKeeper(ZKHOST, SESSIONTIMEOUT, new DistributeLock1());
        System.out.println(zk.getState());

        Stat stat = zk.exists(ROOT_LOCK_PATH, false);
        if (stat == null){
            zk.create(ROOT_LOCK_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
    }

    public boolean tryLock(){
        boolean result = false;
        String splitStr  = "_split_";
        if(lockName.contains(splitStr)){
            System.out.println("error lock name");
            return result;
        }

        try {
            CURRENT_LOCK = zk.create(ROOT_LOCK_PATH  + lockName + splitStr, new byte[0],
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println("create_node:" + CURRENT_LOCK);

            List<String> subNodes = zk.getChildren(ROOT_LOCK_PATH, false);

            List<String> objectLock = new ArrayList<String>();
            for(String node: subNodes){
                String _node = node.split(splitStr)[0];
                if(_node.equals(lockName)){
                    objectLock.add(node);
                }
            }
            Collections.sort(objectLock);
            System.out.println("当前线程的锁是：" + CURRENT_LOCK);

            //当前线程获取了锁
            if(CURRENT_LOCK.equals(ROOT_LOCK_PATH + objectLock.get(0))){
                return true;
            }

            // 若不是最小节点，则找到自己的前一个节点
            String prevNode = CURRENT_LOCK.substring(CURRENT_LOCK.lastIndexOf("/") + 1);
            WAIT_LOCK = objectLock.get(Collections.binarySearch(objectLock, prevNode) - 1);

        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean waitForLock(String prev, long timeWait) throws KeeperException, InterruptedException {
        Stat stat = zk.exists(ROOT_LOCK_PATH + "/" + prev, true);
        if(stat != null){
            System.out.println("等待锁：" + prev);
            this.countDownLatch = new CountDownLatch(1);
            this.countDownLatch.await(timeWait, TimeUnit.MILLISECONDS);
            this.countDownLatch = null;
            System.out.println(Thread.currentThread().getName() + " 等到了锁");
        }
        return true;
    }

    public void unlock() {
        try {
            System.out.println("释放锁 " + CURRENT_LOCK);
            zk.delete(CURRENT_LOCK, -1);
            CURRENT_LOCK = null;
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    public boolean tryLock(long timeout, TimeUnit unit) {
        try {
            if (this.tryLock()) {
                return true;
            }
            return waitForLock(WAIT_LOCK, timeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    @Override
    public void process(WatchedEvent event) {

    }





}
