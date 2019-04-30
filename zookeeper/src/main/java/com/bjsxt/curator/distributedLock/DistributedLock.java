package com.bjsxt.curator.distributedLock;

import com.bjsxt.common.PropertiesUtil;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import java.util.concurrent.TimeUnit;

public class DistributedLock {

    /**
     * zookeeper地址
     */
    static String CONNECT_ADDR;
    /**
     * session超时时间
     */
    static final int SESSION_OUTTIME = 5000;//ms

    private static String LOCK_PATH = "/my/path";


    static {
        CONNECT_ADDR = (String) PropertiesUtil.readPropery().get("zookeeper.url");
    }

    public static void main(String[] args) throws Exception {

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient(CONNECT_ADDR, retryPolicy);
        client.start();

        client.create().forPath(LOCK_PATH, "aa".getBytes());

        InterProcessMutex lock = new InterProcessMutex(client, LOCK_PATH);

        if (lock.acquire(2000, TimeUnit.SECONDS)) {
            try {
                // do some work inside of the critical section here
                System.out.println("获取到了分布式锁了，嘿嘿");
            } finally {
                lock.release();
            }
        }
    }
}
