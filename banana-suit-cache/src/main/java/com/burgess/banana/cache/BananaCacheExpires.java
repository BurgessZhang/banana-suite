package com.burgess.banana.cache;

import com.burgess.banana.common.util.BananaDateUtils;

import java.util.Date;

/**
 * @author burgess.zhang
 * @project banana-suite
 * @package com.burgess.banana.cache
 * @file BananaCacheExpires.java
 * @time 2018-05-16 21:23
 * @desc 缓存有效期设置
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
     * @package
     * @file BananaCacheExpires.java
     * @method todayEndSeconds
     * @author burgess.zhang
     * @time 2018/5/16/016 21:24
     * @desc 当前时间到今天结束相隔的秒
     * @params '[]
     * @result long
     */
    public static long todayEndSeconds() {
        Date curTime = new Date();
        return BananaDateUtils.getDiffSeconds(BananaDateUtils.getDayEnd(curTime), curTime);
    }
}
