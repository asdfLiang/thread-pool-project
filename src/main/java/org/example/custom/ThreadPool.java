package org.example.custom;
/**
 * 自定义线程池
 *
 * @since 2023/3/15 0:04
 * @author by liangzj
 */
public interface ThreadPool {

    /**
     * 执行任务，实际的操作是把任务放到任务队列中
     *
     * @param runnable
     */
    void execute(Runnable runnable);

    /**
     * 线程池是否还有活跃的线程
     *
     * @return
     */
    boolean hasActive();

    /** 停止线程池 */
    void shutdown();
}
