package com.xwm.common.locks;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class LoginLock {
    private static final CyclicBarrier LOCK = new CyclicBarrier(2);
    private static final AtomicInteger LOGIN_SUCCESS = new AtomicInteger(1);

    public static void await(long timeout, TimeUnit unit) throws InterruptedException, BrokenBarrierException, TimeoutException {
        LOCK.await(timeout,unit);
    }

    public static void reset(){
        LOCK.reset();
    }

    public static Integer get(){
        return LOGIN_SUCCESS.get();
    }

    public static void compareAndSet(int expect, int update){
        LOGIN_SUCCESS.compareAndSet(expect,update);
    }
}
