package com.burgess.banana.local;

import com.burgess.banana.common.json.BananaJsonUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.UUID;

/**
 * @author tom.zhang
 * @project banana-suite
 * @package com.burgess.banana.local
 * @file BananaClearCommand.java
 * @time 2018-05-17 17:15
 * @desc 缓存管理操作
 */
public class BananaClearCommand implements Serializable {


    //删除缓存
    private static final byte DELETE_KEY = 0x01;

    //清除缓存
    public static final byte CLEAR = 0x02;

    private String cacheName;
    private String key;
    private String origin;
    private static String CURRENT_NODE_ID;

    static {
        try {
            CURRENT_NODE_ID = InetAddress.getLocalHost().getHostName() + "_"
                    + RandomStringUtils.random(6, true, true).toLowerCase();
        } catch (Exception e) {
            CURRENT_NODE_ID = UUID.randomUUID().toString();
        }
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public BananaClearCommand() {
    }

    public BananaClearCommand(String cacheName, String key) {
        super();
        this.origin = CURRENT_NODE_ID;
        this.cacheName = cacheName;
        this.key = key;
    }

    public String serialize() {
        return BananaJsonUtils.toJson(this);
    }

    public boolean isLocalCommand() {
        return CURRENT_NODE_ID.equals(origin);
    }

    public static BananaClearCommand deserialize(String json) {
        return BananaJsonUtils.toObject(json, BananaClearCommand.class);
    }

}
