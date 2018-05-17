package com.burgess.banana.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.RandomUtils;


/**
 * @author tom.zhang
 * @project banana-suite
 * @package com.burgess.banana.command
 * @file BananaRedisBase.java
 * @time 2018-05-17 18:47
 * @desc redis基础操作指令
 */
public class BananaRedisBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(BananaRedisBase);

    protected static final String RESP_OK = "OK";
    //
    //
    protected String groupName;

    protected byte[] keyBytes;

    protected String key;

    boolean isBinary = true;

    public byte[] getKey() {
        return keyBytes;
    }

    public RedisBase(String key) {
        this(key, null, true);
    }

    public RedisBase(String key, boolean isBinary) {
        this(key, null, isBinary);
    }

    public RedisBase(String key, String groupName, boolean isBinary) {
        this.groupName = groupName;
        this.key = key;
        this.isBinary = isBinary;
        if (isBinary) this.keyBytes = SafeEncoder.encode(key);
    }

    /**
     * 检查给定 key 是否存在。
     *
     * @param keyBytes
     * @return
     */
    public boolean exists() {
        try {
            if (!isBinary) return getJedisCommands(groupName).exists(key);
            if (isCluster(groupName)) {
                return getBinaryJedisClusterCommands(groupName).exists(keyBytes);
            }
            return getBinaryJedisCommands(groupName).exists(keyBytes);
        } finally {
            getJedisProvider(groupName).release();
        }

    }


    /**
     * 删除给定的一个 key 。
     * <p>
     * 不存在的 key 会被忽略。
     *
     * @param keyBytes
     * @return true：存在该key删除时返回
     * <p>
     * false：不存在该key
     */
    public boolean remove() {
        try {
            if (!isBinary) return getJedisCommands(groupName).del(key) == 1;
            if (isCluster(groupName)) {
                return getBinaryJedisClusterCommands(groupName).del(keyBytes) == 1;
            }
            return getBinaryJedisCommands(groupName).del(keyBytes) == 1;
        } finally {
            getJedisProvider(groupName).release();
        }
    }

    /**
     * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。
     *
     * @param keyBytes
     * @param seconds  超时时间，单位：秒
     * @return true：超时设置成功
     * <p>
     * false：key不存在或超时未设置成功
     */
    public boolean setExpire(long seconds) {
        if (seconds <= 0) return true;
        try {
            if (!isBinary) return getJedisCommands(groupName).expire(key, (int) seconds) == 1;
            if (isCluster(groupName)) {
                return getBinaryJedisClusterCommands(groupName).expire(keyBytes, (int) seconds) == 1;
            }
            return getBinaryJedisCommands(groupName).expire(keyBytes, (int) seconds) == 1;
        } finally {
            getJedisProvider(groupName).release();
        }

    }

    /**
     * 设置指定时间戳时失效
     * <p>
     * 注意：redis服务器时间问题
     *
     * @param keyBytes
     * @param expireAt 超时时间点
     * @return true：超时设置成功
     * <p>
     * false：key不存在或超时未设置成功
     */
    public boolean setExpireAt(Date expireAt) {
        try {
            if (!isBinary) return getJedisCommands(groupName).pexpireAt(key, expireAt.getTime()) == 1;
            if (isCluster(groupName)) {
                return getBinaryJedisClusterCommands(groupName).pexpireAt(keyBytes, expireAt.getTime()) == 1;
            }
            return getBinaryJedisCommands(groupName).pexpireAt(keyBytes, expireAt.getTime()) == 1;
        } finally {
            getJedisProvider(groupName).release();
        }
    }

    /**
     * 没设置过期时间则设置
     *
     * @param seconds
     * @return
     */
    public boolean setExpireIfNot(long seconds) {
        Long ttl = getTtl();
        if (ttl == -1) {
            return setExpire(seconds);
        }
        return ttl >= 0;
    }

    /**
     * 返回给定 key 的剩余生存时间(单位：秒)
     *
     * @param keyBytes
     * @return 当 key 不存在时，返回 -2 。
     * 当 key 存在但没有设置剩余生存时间时，返回 -1 。
     * 否则返回 key的剩余生存时间。
     */
    public Long getTtl() {
        try {
            if (!isBinary) return getJedisCommands(groupName).ttl(key);
            long result = 0;
            if (isCluster(groupName)) {
                result = getBinaryJedisClusterCommands(groupName).ttl(keyBytes);
            } else {
                result = getBinaryJedisCommands(groupName).ttl(keyBytes);
            }
            return result;
        } finally {
            getJedisProvider(groupName).release();
        }

    }

    /**
     * 移除给定 key 的生存时间，设置为永久有效
     *
     * @param keyBytes
     * @return 当生存时间移除成功时，返回 1 .
     * <p>
     * 如果 key 不存在或 key 没有设置生存时间，返回 0 。
     */
    public boolean removeExpire() {
        try {
            if (!isBinary) return getJedisCommands(groupName).persist(key) == 1;
            if (isCluster(groupName)) {
                return getBinaryJedisClusterCommands(groupName).persist(keyBytes) == 1;
            }
            return getBinaryJedisCommands(groupName).persist(keyBytes) == 1;
        } finally {
            getJedisProvider(groupName).release();
        }
    }

    /**
     * 返回 key 所储存的值的类型。
     *
     * @param keyBytes
     * @return none (key不存在)
     * <p>
     * string (字符串)
     * <p>
     * list (列表)
     * <p>
     * set (集合)
     * <p>
     * zset (有序集)
     * <p>
     * hash (哈希表)
     */
    public String type() {
        try {
            if (isCluster(groupName)) {
                return getBinaryJedisClusterCommands(groupName).type(keyBytes);
            }
            return getBinaryJedisCommands(groupName).type(keyBytes);
        } finally {
            getJedisProvider(groupName).release();
        }

    }

    /**
     * 查找所有符合给定模式 pattern 的 key
     *
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern) {
        Set<String> keys = JedisProviderFactory.getMultiKeyCommands(groupName).keys(pattern);
        return keys;
    }

    protected byte[] valueSerialize(Object value) {
        try {
            return SerializeUtils.serialize(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected byte[][] valuesSerialize(Object... objects) {
        try {
            byte[][] many = new byte[objects.length][];
            for (int i = 0; i < objects.length; i++) {
                many[i] = SerializeUtils.serialize(objects[i]);
            }
            return many;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T valueDerialize(byte[] bytes) {
        if (bytes == null) return null;
        try {
            return (T) SerializeUtils.deserialize(bytes);
        } catch (Exception e) {
            remove();
            logger.warn("get key[{}] from redis is not null,but Deserialize error,message:{}", key, e);
            return null;
        }
    }

    protected <T> List<T> listDerialize(List<byte[]> datas) {
        List<T> list = new ArrayList<>();
        if (datas == null) return list;
        for (byte[] bs : datas) {
            list.add((T) valueDerialize(bs));
        }
        return list;
    }

    /**
     * 默认过期时间
     *
     * @return
     */
    public static long getDefaultExpireSeconds() {
        return CacheExpires.IN_1WEEK + RandomUtils.nextLong(1, CacheExpires.IN_1DAY);
    }
}
