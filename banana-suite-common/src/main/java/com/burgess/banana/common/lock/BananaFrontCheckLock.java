package com.burgess.banana.common.lock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author burgess.zhang
 * @project banana-suite
 * @package com.burgess.banana.common.lock
 * @file BananaFrontCheckLock.java
 * @time 2018-05-16 20:59
 * @desc 本地前置检查锁
 */
public class BananaFrontCheckLock extends ReentrantLock {

    private static final long serialVersionUID = 1L;

    private static BananaFrontCheckLock context = new BananaFrontCheckLock();

    private Map<String, BananaFrontCheckLock> localLocks = new ConcurrentHashMap<>();

    private String name;
    private AtomicInteger count = new AtomicInteger(0);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AtomicInteger getCount() {
        return count;
    }

    public int updateCount(int delta) {
        return this.count.addAndGet(delta);
    }

    public static boolean lock(String lockName, long timeout, TimeUnit unit) {
        BananaFrontCheckLock lc = context.localLocks.get(lockName);
        if (lc == null) {
            synchronized (context.localLocks) {
                lc = context.localLocks.get(lockName);
                if (lc == null) {
                    lc = new BananaFrontCheckLock();
                    context.localLocks.put(lockName, lc);
                }
            }
        }
        lc.updateCount(1);

        try {
            return lc.tryLock(timeout, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    public static void unlock(String lockName) {
        BananaFrontCheckLock lc = context.localLocks.get(lockName);
        if (lc != null) {
            lc.unlock();
            if (lc.updateCount(-1) == 0) {
                context.localLocks.remove(lockName);
                lc = null;
            }
        }
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BananaFrontCheckLock other = (BananaFrontCheckLock) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}
