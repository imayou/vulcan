package io.ayou.vulcan.util.thread.pool;

/**
 * 当Queue中的runnable达到了limit上限时，决定采用何种策略通知提交者
 */
@FunctionalInterface
public interface DenyPolicy {
    void reject(Runnable runnable, ThreadPool threadPool);

    /**
     * 直接丢弃
     */
    class DiscardDenyPolicy implements DenyPolicy {
        @Override
        public void reject(Runnable runnable, ThreadPool threadPool) {
            //
        }
    }

    /**
     * 向任务提交者抛出异常
     */
    class AbortDenyPolicy implements DenyPolicy {
        @Override
        public void reject(Runnable runnable, ThreadPool threadPool) {
            throw new RunnableDenyException("The runnable " + runnable + " will be abort");
        }
    }

    /**
     * 在提交者所在的线程中执行
     */
    class RunnerDenyPolicy implements DenyPolicy {
        @Override
        public void reject(Runnable runnable, ThreadPool threadPool) {
            if (!threadPool.isShutdown()) {
                runnable.run();
            }
        }
    }
}
