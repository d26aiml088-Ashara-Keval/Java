package Lab_09;

import java.util.Arrays;

class SharedTotal {
    long total = 0;

    void addUnsafe(long value) {
        total += value;
    }

    synchronized void addSafe(long value) {
        total += value;
    }
}

public class ArrayTotalRace {
    static final int SIZE = 1000000;
    static final int THREADS = 4;

    static long calculate(long[] arr, boolean safe)
            throws InterruptedException {
        SharedTotal shared = new SharedTotal();
        Thread[] threads = new Thread[THREADS];

        for (int i = 0; i < THREADS; i++) {
            final int start = i * (SIZE / THREADS);
            final int end = (i + 1) * (SIZE / THREADS);

            threads[i] = new Thread(() -> {
                for (int j = start; j < end; j++) {
                    if (safe) {
                        shared.addSafe(arr[j]);
                    } else {
                        shared.addUnsafe(arr[j]);
                    }
                }
            });

            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        return shared.total;
    }

    static long calculateAtomic(long[] arr)
            throws InterruptedException {
        java.util.concurrent.atomic.AtomicLong total =
                new java.util.concurrent.atomic.AtomicLong(0);

        Thread[] threads = new Thread[THREADS];

        for (int i = 0; i < THREADS; i++) {
            final int start = i * (SIZE / THREADS);
            final int end = (i + 1) * (SIZE / THREADS);

            threads[i] = new Thread(() -> {
                for (int j = start; j < end; j++) {
                    total.addAndGet(arr[j]);
                }
            });

            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        return total.get();
    }

    public static void main(String[] args)
            throws InterruptedException {
        long[] arr = new long[SIZE];
        Arrays.fill(arr, 1);

        long expected = SIZE;

        long start = System.nanoTime();
        long unsafe = calculate(arr, false);
        long unsafeTime = System.nanoTime() - start;

        start = System.nanoTime();
        long synchronizedTotal = calculate(arr, true);
        long synchronizedTime = System.nanoTime() - start;

        start = System.nanoTime();
        long atomicTotal = calculateAtomic(arr);
        long atomicTime = System.nanoTime() - start;

        System.out.println("Expected total: " + expected);
        System.out.println("Without synchronization: " + unsafe);
        System.out.println("Synchronized total: " + synchronizedTotal);
        System.out.println("Atomic total: " + atomicTotal);

        System.out.println("\nExecution times:");
        System.out.println("Unsafe: " + unsafeTime / 1_000_000.0 + " ms");
        System.out.println("Synchronized: "
                + synchronizedTime / 1_000_000.0 + " ms");
        System.out.println("Atomic: " + atomicTime / 1_000_000.0 + " ms");
    }
}
