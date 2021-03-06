//package com.neo.util;
//
//import com.hivescm.wms.common.distributed.model.DataCenterModel;
//import com.hivescm.wms.common.distributed.queue.ConcurrentCircularQueue;
//
//import java.time.Instant;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
///**
// * Twitter_Snowflake,算法封装，支持自适应实例数量 <br>
// *
// * @author aa
// * SnowFlake的结构如下(每部分用-分开):<br>
// * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
// * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0<br>
// * 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
// * 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
// * 10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId<br>
// * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号<br>
// * 加起来刚好64位，为一个Long型。<br>
// * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
// */
//public class SnowflakeIdWorkerWrapper {
//    private static Object lock = new Object();
//
//    private static List<DataCenterModel> dataCenterModelList;
//
//    private static ConcurrentCircularQueue<SnowflakeIdWorkerWrapper> workerQueue = new ConcurrentCircularQueue<SnowflakeIdWorkerWrapper>();
//
//    public static SnowflakeIdWorkerWrapper getWorker() {
//        if (workerQueue.size() < 1) {
//            if (dataCenterModelList == null || dataCenterModelList.size() < 1) {
//                updateCacheSimple();
//            } else {
//                updateCache(dataCenterModelList);
//            }
//        }
//        return workerQueue.next();
//    }
//
//    private static void updateCacheSimple() {
//        synchronized (lock) {
//            if (workerQueue.size() > 0) {
//                return;
//            }
//            for (int w = 0; w <= MAX_WORKER_ID; w++) {
//                for (int d = 0; d <= MAX_DATACENTER_ID; d++) {
//                    workerQueue.add(new SnowflakeIdWorkerWrapper(w, d));
//                }
//            }
//        }
//    }
//
//    public static void updateCache(List<DataCenterModel> dataCenterModelList) {
//        synchronized (lock) {
//            if (dataCenterModelList == null || dataCenterModelList.size() < 1) {
//                SnowflakeIdWorkerWrapper.dataCenterModelList = null;
//                return;
//            } else {
//                SnowflakeIdWorkerWrapper.dataCenterModelList = dataCenterModelList;
//                ConcurrentCircularQueue<SnowflakeIdWorkerWrapper> workerQueueTemp = new ConcurrentCircularQueue<SnowflakeIdWorkerWrapper>();
//                for (DataCenterModel model : dataCenterModelList) {
//                    workerQueueTemp.add(new SnowflakeIdWorkerWrapper(model.getWorkerId(), model.getDataCenterId()));
//                }
//                workerQueue = workerQueueTemp;
//            }
//        }
//    }
//
//    // ==============================Fields===========================================
//    /**
//     * 开始时间截 (2015-01-01)
//     */
//    private final long twepoch = 1420041600000L;
//
//    /**
//     * 机器id所占的位数
//     */
//    private final static long workerIdBits = 5L;
//
//    /**
//     * 数据标识id所占的位数
//     */
//    private final static long datacenterIdBits = 5L;
//
//    /**
//     * 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
//     */
//    public final static long MAX_WORKER_ID = -1L ^ (-1L << workerIdBits);
//
//    /**
//     * 支持的最大数据标识id，结果是31
//     */
//    public final static long MAX_DATACENTER_ID = -1L ^ (-1L << datacenterIdBits);
//
//    /**
//     * 序列在id中占的位数
//     */
//    private final long sequenceBits = 12L;
//
//    /**
//     * 机器ID向左移12位
//     */
//    private final long workerIdShift = sequenceBits;
//
//    /**
//     * 数据标识id向左移17位(12+5)
//     */
//    private final long datacenterIdShift = sequenceBits + workerIdBits;
//
//    /**
//     * 时间截向左移22位(5+5+12)
//     */
//    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
//
//    /**
//     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
//     */
//    private final long sequenceMask = -1L ^ (-1L << sequenceBits);
//
//    /**
//     * 工作机器ID(0~31)
//     */
//    private long workerId;
//
//    /**
//     * 数据中心ID(0~31)
//     */
//    private long datacenterId;
//
//    /**
//     * 毫秒内序列(0~4095)
//     */
//    private long sequence = 0L;
//
//    /**
//     * 上次生成ID的时间截
//     */
//    private long lastTimestamp = -1L;
//
//    //==============================Constructors=====================================
//
//    /**
//     * 构造函数
//     *
//     * @param workerId     工作ID (0~31)
//     * @param datacenterId 数据中心ID (0~31)
//     */
//    public SnowflakeIdWorkerWrapper(long workerId, long datacenterId) {
//        if (workerId > MAX_WORKER_ID || workerId < 0) {
//            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
//        }
//        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
//            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", MAX_DATACENTER_ID));
//        }
//        this.workerId = workerId;
//        this.datacenterId = datacenterId;
//    }
//
//    // ==============================Methods==========================================
//
//    /**
//     * 获得下一个ID (该方法是线程安全的)
//     *
//     * @return SnowflakeId
//     */
//    public synchronized long nextId() {
//        long timestamp = timeGen();
//
//        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
//        if (timestamp < lastTimestamp) {
//            throw new RuntimeException(
//                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
//        }
//
//        //如果是同一时间生成的，则进行毫秒内序列
//        if (lastTimestamp == timestamp) {
//            sequence = (sequence + 1) & sequenceMask;
//            //毫秒内序列溢出
//            if (sequence == 0) {
//                //阻塞到下一个毫秒,获得新的时间戳
//                timestamp = tilNextMillis(lastTimestamp);
//            }
//        }
//        //时间戳改变，毫秒内序列重置
//        else {
//            sequence = 0L;
//        }
//
//        //上次生成ID的时间截
//        lastTimestamp = timestamp;
//
//        //移位并通过或运算拼到一起组成64位的ID
//        return ((timestamp - twepoch) << timestampLeftShift)
//                | (datacenterId << datacenterIdShift)
//                | (workerId << workerIdShift)
//                | sequence;
//    }
//
//    /**
//     * 阻塞到下一个毫秒，直到获得新的时间戳
//     *
//     * @param lastTimestamp 上次生成ID的时间截
//     * @return 当前时间戳
//     */
//    protected long tilNextMillis(long lastTimestamp) {
//        long timestamp = timeGen();
//        while (timestamp <= lastTimestamp) {
//            timestamp = timeGen();
//        }
//        return timestamp;
//    }
//
//    /**
//     * 返回以毫秒为单位的当前时间
//     *
//     * @return 当前时间(毫秒)
//     */
//    protected long timeGen() {
//        return System.currentTimeMillis();
//    }
//
//    //==============================Test=============================================
//
//    /**
//     * 测试
//     */
//    public static void main(String[] args) {
//
//        SnowflakeIdWorkerWrapper idWorker = SnowflakeIdWorkerWrapper.getWorker();
//        long currentTime = System.currentTimeMillis();
//        int count = 0;
//        System.out.println(Instant.now());
//        Set<Long> longSet = new HashSet<>();
//        while (System.currentTimeMillis() - currentTime < 10000) {
//            long id = idWorker.nextId();
//            longSet.add(id);
//            count++;
//        }
//        System.out.println(Instant.now());
//        if (longSet.size() != count) {
//            System.out.println("生成有重复");
//        }
//        System.out.println("10秒内共生成" + count + "个id，平均每秒生成" + (count / 10) + "个。");
//
//        //for (int i = 0; i < 1000; i++) {
//        //    long id = idWorker.nextId();
//        //    System.out.println(Long.toBinaryString(id));
//        //    System.out.println(id);
//        //}
//    }
//}