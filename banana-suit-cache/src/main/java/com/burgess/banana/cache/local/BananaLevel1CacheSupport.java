package com.burgess.banana.cache.local;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

/**
 * @author burgess.zhang
 * @project banana-suite
 * @package com.burgess.banana.cache.local
 * @file BananaLevel1CacheSupport.java
 * @time 2018-05-16 21:31
 * @desc 本地缓存同步处理器
 */
public class BananaLevel1CacheSupport implements InitializingBean, DisposableBean{
}
