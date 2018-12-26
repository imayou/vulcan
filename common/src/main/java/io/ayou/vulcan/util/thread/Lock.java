package io.ayou.vulcan.util.thread;

import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * @author AYOU
 * @ClassName Lock
 */
public interface Lock {
    /**
     * 永远阻塞类似synchronized，但是此方法可以被中断，中断时抛出InterruptedException
     * @throws InterruptedException
     */
    void lock() throws InterruptedException;

    /**
     * 除了能中断，增加超时功能
     * @param mills
     * @throws InterruptedException
     * @throws TimeoutException
     */
    void lock(long mills) throws InterruptedException, TimeoutException;

    /**
     * 释放锁
     */
    void unlock();

    /**
     * 获取当前被阻塞的线程
     * @return
     */
    List<Thread> getBlockedThreads();
}
