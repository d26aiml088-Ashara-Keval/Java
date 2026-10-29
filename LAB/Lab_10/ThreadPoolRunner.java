package Lab_10;

import java.util.concurrent.*;

public class ThreadPoolRunner {
    public static void main(String[] args)
            throws InterruptedException {

        ExecutorService pool = Executors.newFixedThreadPool(3);

        for (int i = 1; i <= 10; i++) {
            final int id = i;

            pool.submit(() -> {
                System.out.println(
                    "Task " + id + " running on "
                    + Thread.currentThread().getName());

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("All tasks completed.");
    }
}