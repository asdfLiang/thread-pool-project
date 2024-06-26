package org.example.custom;

/**
 * 自定线程池测试
 *
 * @since 2023/3/15 0:05
 * @author by liangzj
 */
public class Main {

    public static void main(String[] args) {
        ThreadPool threadPool = new SimpleThreadPool(5, null);

        for (int i = 0; i < 10; i++) {
            threadPool.execute(
                    () -> System.out.println("It is print by " + Thread.currentThread().getName()));
        }

        threadPool.shutdown();
    }
}
