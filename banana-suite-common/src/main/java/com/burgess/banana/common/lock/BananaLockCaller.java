package com.burgess.banana.common.lock;

/**
 * @author burgess.zhang
 * @project banana-suite
 * @package com.burgess.banana.common.lock
 * @file BananaLockCaller.java
 * @time 2018-05-16 20:58
 * @desc 锁操作接口
 */
public interface BananaLockCaller<T> {

    /**
     * 持有锁的操作
     */
    T onHolder();

    /**
     * 等待锁的操作
     */
    T onWait();
}
