package io.ayou.vulcan.util.thread.pool;


import java.util.LinkedList;

/**
 * @ClassName LinkedRunnableQueue
 */
public class LinkedRunnableQueue implements RunnableQueue {

    /**
     * 任务对列的最大容量，在构造时传入
     */
    private final int limit;
    /**
     * 若任务队列中的任务已经满了，则需要执行拒绝策略
     */
    private final DenyPolicy denyPolicy;
    /**
     * 存放队列的任务
     */
    private final LinkedList<Runnable> runnableList = new LinkedList<>();
    private final ThreadPool threadPool;

    public LinkedRunnableQueue(int limit, DenyPolicy denyPolicy, ThreadPool threadPool) {
        this.limit = limit;
        this.denyPolicy = denyPolicy;
        this.threadPool = threadPool;
    }

    @Override
    public void offer(Runnable runnable) {
        synchronized (runnableList) {
            if (runnableList.size() >= limit) {
                denyPolicy.reject(runnable, threadPool);
            } else {
                runnableList.addLast(runnable);
                runnableList.notifyAll();
            }
        }
    }

    @Override
    public Runnable take() throws InterruptedException {
        synchronized (runnableList) {
            while (runnableList.isEmpty()) {
                try {
                    //如果任务队列中没有可执行任务，则当前线程将会挂起
                    //进入runnableList关联的monitor waitset中等待唤醒(新的任务加入)
                    runnableList.wait();
                } catch (InterruptedException e) {
                    throw e;
                }
            }
        }
        //从任务队列头部移除一个任务
        return runnableList.removeFirst();
    }

    @Override
    public int size() {
        synchronized (runnableList){
            //返回当前任务队列中的任务数
            return runnableList.size();
        }
    }
}
