package com.burgess.banana.exception;

import com.burgess.banana.common.util.BananaDateUtils;

import java.util.Date;

/**
 * @author tom.zhang
 * @project banana-suite
 * @package com.burgess.banana.exception
 * @file BananaCacheExpires.java
 * @time 2018-05-17 17:11
 * @desc 缓存有效期常量
 */
public class BananaCacheExpires {

    public final static long IN_1MIN = 60;

    public final static long IN_3MINS = 60 * 3;

    public final static long IN_5MINS = 60 * 5;

    public final static long IN_1HOUR = 60 * 60;

    public final static long IN_HALF_HOUR = 60 * 30;

    public final static long IN_1DAY = IN_1HOUR * 24;

    public final static long IN_1WEEK = IN_1DAY * 7;

    public final static long IN_1MONTH = IN_1DAY * 30;

    /**
     * @param '[]
     * @return long
     * @class_name BananaCacheExpires
     * @method todayEndSeconds
     * @desc 当前时间到今天结束相隔的秒
     * @author free.zhang
     * @date 2018/5/17 17:14
     */
    public static long todayEndSeconds() {
        Date curTime = new Date();
        return BananaDateUtils.getDiffSeconds(BananaDateUtils.getDayEnd(curTime), curTime);
    }

}
