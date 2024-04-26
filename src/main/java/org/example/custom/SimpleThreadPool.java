package org.example.custom;

import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @since 2023/3/15 0:05
 * @author by liangzj
 */
public class SimpleThreadPool implements ThreadPool {

    /** 任务队列 */
    private final LinkedBlockingQueue<Runnable> taskQueue;

    /** 工作线程数 */
    private final int coreSize;

    /** 线程池是否运行 */
    private boolean running;

    /** 活跃线程数 */
    private final AtomicInteger activeCount;

    public SimpleThreadPool(int coreSize, LinkedBlockingQueue<Runnable> taskQueue) {
        this.coreSize = coreSize;
        this.taskQueue = Optional.ofNullable(taskQueue).orElseGet(LinkedBlockingQueue::new);
        this.running = true;
        this.activeCount = new AtomicInteger(0);

        // 创建n个线程轮询任务队列
        for (int i = 0; i < coreSize; i++) {
            createPollTaskQueueThread(i);
        }
    }

    @Override
    public void execute(Runnable runnable) {
        taskQueue.offer(runnable);
    }

    @Override
    public boolean hasActive() {
        return activeCount.get() > 0;
    }

    @Override
    public void shutdown() {
        // 如果还有任务，等任务都完成再关闭线程池
        while (hasActive()) {}
        this.running = false;
    }

    /**
     * 创建轮询任务队列的线程
     *
     * @param i
     */
    private void createPollTaskQueueThread(int i) {
        new Thread(
                        () -> {
                            // 一直轮询任务队列，有任务就执行，没有就阻塞，这就是为什么线程也不会被释放，被阻塞队列阻塞了
                            while (running) {
                                // 没有任务这里会阻塞，taskQueue是个阻塞队列
                                Runnable task = taskQueue.poll();
                                // 拿到任务，执行任务的run方法
                                activeCount.addAndGet(1);
                                Optional.ofNullable(task).ifPresent(Runnable::run);
                                activeCount.addAndGet(-1);
                            }
                            System.out.println(Thread.currentThread().getName() + " stop.");
                        },
                        "Thread-" + i)
                .start();
    }
}
