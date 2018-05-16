package com.burgess.banana.common.sequence;

import com.burgess.banana.common.util.BananaNodeNameHolder;
import org.apache.commons.lang3.RandomUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * @author burgess.zhang
 * @project banana-suite
 * @package com.burgess.banana.common.sequence
 * @file BananaSnowflakeGenerator.java
 * @time 2018-05-16 20:49
 * @desc 全局ID生成器
 */
public class BananaSnowflakeGenerator implements BananaIdGenerator, Watcher{

    private static final String ROOT_PATH = "/global_apps/nodes";

    private long workerId;
    private long datacenterId;
    private long sequence = 0L;

    private long twepoch = 1288834974657L;

    private long workerIdBits = 5L;
    private long datacenterIdBits = 5L;
    private long maxWorkerId = -1L ^ (-1L << workerIdBits);
    private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    private long sequenceBits = 12L;

    private long workerIdShift = sequenceBits;
    private long datacenterIdShift = sequenceBits + workerIdBits;
    private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    private long sequenceMask = -1L ^ (-1L << sequenceBits);

    private long lastTimestamp = -1L;

    private ZooKeeper zk;

    public BananaSnowflakeGenerator(String zkServer) {
        try {
            zk = new ZooKeeper(zkServer, 10000, this);
            Stat stat = zk.exists(ROOT_PATH, false);
            if (stat == null) {
                zk.create(ROOT_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            String nodePath = ROOT_PATH + "/" + BananaNodeNameHolder.getNodeId();
            zk.create(nodePath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            int workerId = zk.getChildren(ROOT_PATH, false).size() + 1;
            if (workerId > maxWorkerId || workerId < 0) {
                throw new IllegalArgumentException(
                        String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
            }
            this.workerId = workerId;
        } catch (Exception e) {
            this.workerId = RandomUtils.nextInt(1, 31);
        }
        this.datacenterId = 1;
    }

    public BananaSnowflakeGenerator(long workerId, long datacenterId) {
        // sanity check for workerId
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(
                    String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(
                    String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    @Override
    public synchronized long nextId() {
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format(
                    "Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift) | sequence;
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }

    @Override
    public void process(WatchedEvent event) {

    }

    @Override
    public void close(){
        try {
            zk.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BananaSnowflakeGenerator generator = new BananaSnowflakeGenerator(32, 32);
        System.out.println(generator.nextId());
    }
}
