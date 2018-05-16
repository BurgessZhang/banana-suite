package com.burgess.banana.common.util;


import java.net.InetAddress;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author tom.zhang
 * @project banana-suite
 * @package com.burgess.banana.common.util
 * @file BananaNodeNameHolder.java
 * @time 2018-05-16 17:18
 * @desc
 */
public class BananaNodeNameHolder {

    private static String nodeId;

    public static String getNodeId() {
        if (nodeId != null) return nodeId;
        try {
            nodeId = InetAddress.getLocalHost().getHostAddress() + "_" + RandomStringUtils.random(3, true, true).toLowerCase();
        } catch (Exception e) {
            nodeId = UUID.randomUUID().toString();
        }
        return nodeId;
    }
}
