package io.ayou.vulcan.util.thread.pool;

/**
 *工厂
 */
@FunctionalInterface
public interface ThreadFactory {
    /**
     * 创建线程
     * @param runnable
     * @return
     */
    Thread createThread(Runnable runnable);
}
