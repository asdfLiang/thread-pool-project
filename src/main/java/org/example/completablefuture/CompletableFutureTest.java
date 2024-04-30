package org.example.completablefuture;

import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * @since 2024/4/26 下午10:05
 * @author by liangzj
 */
public class CompletableFutureTest {

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newCachedThreadPool();

        System.out.println("CPU核心数：" + Runtime.getRuntime().availableProcessors());
        System.out.println("ForkJoinPool最大线程数：" + ForkJoinPool.getCommonPoolParallelism());
        System.out.println("ForkJoinPool当前线程数：" + ForkJoinPool.commonPool().getPoolSize());

        long start = System.currentTimeMillis();
        CompletableFuture.allOf(
                        IntStream.range(0, 1000)
                                .mapToObj(i -> new Task("task-" + i, 1))
                                .map(task -> CompletableFuture.runAsync(task::execute, threadPool))
                                .toArray(CompletableFuture[]::new))
                .join();
        System.out.println("cost: " + (System.currentTimeMillis() - start) / 1000 + "s");
        System.out.println("ForkJoinPool当前线程数：" + ForkJoinPool.commonPool().getPoolSize());

        threadPool.shutdown();
    }

    private record Task(String name, long executeTime) {

        public void execute() {
            try {
                TimeUnit.SECONDS.sleep(executeTime);
                System.out.println(name + " finished");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
