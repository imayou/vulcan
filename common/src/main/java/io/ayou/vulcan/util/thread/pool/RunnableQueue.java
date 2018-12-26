package io.ayou.vulcan.util.thread.pool;

/**
 * 任务对列，主要用于缓存提交到线程池中的任务
 * @author AYOU
 */
public interface RunnableQueue {
    /**
     * 当有新的任务进来时首先会offer到队列中
     */
    void offer(Runnable runnable);

    /**
     * 工作线程通过take方法获取Runnable
     *
     * @return
     */
    Runnable take();

    /**
     * 获取任务对列中任务的数量
     *
     * @return
     */
    int size();
}
