package com.burgess.banana.local;

import java.io.Closeable;

/**
 * @author tom.zhang
 * @project banana-suite
 * @package com.burgess.banana.local
 * @file BananaLevel1CacheProvider.java
 * @time 2018-05-17 17:33
 * @desc
 */
public interface BananaLevel1CacheProvider extends Closeable {

    void start();

    boolean set(String cacheName, String key, Object value);

    <T> T get(String cacheName, String key);

    void remove(String cacheName, String key);

    void remove(String cacheName);

    void clearAll();
}
