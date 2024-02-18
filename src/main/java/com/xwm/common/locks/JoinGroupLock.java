package com.xwm.common.locks;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class JoinGroupLock {
    private static final CountDownLatch LOCK = new CountDownLatch(1);

    public static void await(long timeout, TimeUnit unit) throws InterruptedException {
        LOCK.await(timeout,unit);
    }

    public static void countDown(){
        LOCK.countDown();
    }

    public static void await() throws InterruptedException {
        LOCK.await(60L,TimeUnit.SECONDS);
    }

}
