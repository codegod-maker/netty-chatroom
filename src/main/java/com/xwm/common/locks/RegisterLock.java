package com.xwm.common.locks;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RegisterLock {
    private static final CountDownLatch LOCK = new CountDownLatch(1);
    private static final AtomicInteger REGISTER_SUCCESS = new AtomicInteger(1);

    public static void await(long timeout, TimeUnit unit) throws InterruptedException {
        LOCK.await(timeout,unit);
    }

    public static void countDown(){
        LOCK.countDown();
    }

    public static Integer get(){
        return REGISTER_SUCCESS.get();
    }

    public static void compareAndSet(int expect, int update){
        REGISTER_SUCCESS.compareAndSet(expect,update);
    }
}
