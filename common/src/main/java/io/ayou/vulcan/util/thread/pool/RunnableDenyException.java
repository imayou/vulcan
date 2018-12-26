package io.ayou.vulcan.util.thread.pool;

/**
 * @ClassName RunnableDenyException
 * 通知任务提交者，任务对列已无法再接受新的任务
 */
public class RunnableDenyException extends RuntimeException {
    public RunnableDenyException(String message) {
        super(message);
    }
}
