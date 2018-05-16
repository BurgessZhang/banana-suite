package com.burgess.banana.common.lock;

import com.burgess.banana.common.exception.BananaSuiteException;

/**
 * @author burgess.zhang
 * @project banana-suite
 * @package com.burgess.banana.common.lock
 * @file BananaLockException.java
 * @time 2018-05-16 20:57
 * @desc 自定义锁异常
 */
public class BananaLockException extends BananaSuiteException {

    private static final long serialVersionUID = 1L;

    public BananaLockException(String e) {
        super(9999,e);
    }

    public BananaLockException(Throwable cause) {
        super(9999, cause.getMessage(), cause);
    }
}
