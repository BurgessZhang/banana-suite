package com.burgess.banana.common.lock;

import com.burgess.banana.common.lock.redis.BananaRedisDistributeLock;

/**
 * @author burgess.zhang
 * @project banana-suite
 * @package com.burgess.banana.common.lock
 * @file BananaDistributeLockTemplate.java
 * @time 2018-05-16 21:01
 * @desc
 */
public class BananaDistributeLockTemplate {

    private static final long _DEFAULT_LOCK_HOLD_MILLS = 30000;


    public static <T> T execute(String lockId,BananaLockCaller<T> caller){
        return execute(lockId, caller, _DEFAULT_LOCK_HOLD_MILLS);
    }

    /**
     * @package
     * @file BananaDistributeLockTemplate.java
     * @method execute
     * @author burgess.zhang
     * @time 2018/5/16/016 21:02
     * @desc
     * @params '[lockId 要确保不和其他业务冲突（不能用随机生成）, caller  业务处理器, timeout 超时时间（毫秒）]
     * @result T
     */
    public static <T> T execute(String lockId,BananaLockCaller<T> caller,long timeout){

        BananaRedisDistributeLock dLock = new BananaRedisDistributeLock(lockId,(int)timeout/1000);

        boolean getLock = false;
        try {
            if(dLock.tryLock()){
                getLock = true;
                return caller.onHolder();
            }else{
                return caller.onWait();
            }
        } finally {
            if(getLock)dLock.unlock();
        }

    }
}
