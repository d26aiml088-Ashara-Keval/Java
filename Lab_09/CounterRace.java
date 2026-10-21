package Lab_09;

class Counter {
    int count = 0;

    void increment() {
        count++;
    }

    synchronized void safeIncrement() {
        count++;
    }
}

public class CounterRace {
    public static void main(String[] args)
            throws InterruptedException {
        int threads = 10;
        int increments = 100000;

        Counter c = new Counter();
        Thread[] t = new Thread[threads];

        for (int i = 0; i < threads; i++) {
            t[i] = new Thread(() -> {
                for (int j = 0; j < increments; j++) {
                    c.increment();
                }
            });
            t[i].start();
        }

        for (Thread thread : t) {
            thread.join();
        }

        System.out.println("Without synchronization: " + c.count);

        final Counter finalCounter = new Counter();
        Counter safeCounter = finalCounter;

        for (int i = 0; i < threads; i++) {
            t[i] = new Thread(() -> {
                for (int j = 0; j < increments; j++) {
                    safeCounter.safeIncrement();
                }
            });
            t[i].start();
        }

        for (Thread thread : t) {
            thread.join();
        }

        System.out.println("With synchronization: " + finalCounter.count);
        System.out.println("Expected: " + (threads * increments));
    }
}