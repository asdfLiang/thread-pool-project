package org.example.demo;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池示例
 *
 * @since 2023/10/22 7:52
 * @author by liangzj
 */
public class Main {

    private static final ThreadPoolExecutor threadPool =
            new ThreadPoolExecutor(
                    5,
                    5,
                    10,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(10),
                    new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            threadPool.execute(
                    () -> System.out.println("It is print by " + Thread.currentThread().getName()));
        }

        threadPool.shutdown();
    }
}
