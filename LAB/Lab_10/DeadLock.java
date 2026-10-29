package Lab_10;

class DeadlockDemo {
    private static final Object lockA = new Object();
    private static final Object lockB = new Object();

    static void createDeadlock() {
        Thread t1 = new Thread(() -> {
            synchronized (lockA) {
                System.out.println("Thread 1 locked A");

                sleep();

                synchronized (lockB) {
                    System.out.println("Thread 1 locked B");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (lockB) {
                System.out.println("Thread 2 locked B");

                sleep();

                synchronized (lockA) {
                    System.out.println("Thread 2 locked A");
                }
            }
        });

        t1.start();
        t2.start();
    }

    static void fixedLockOrder() {
        Thread t1 = new Thread(() -> {
            synchronized (lockA) {
                synchronized (lockB) {
                    System.out.println("Thread 1 completed safely");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (lockA) {
                synchronized (lockB) {
                    System.out.println("Thread 2 completed safely");
                }
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    static void sleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        System.out.println("Demonstrating deadlock...");
        createDeadlock();

        // Run the fixed version separately to avoid the
        // deadlocked threads keeping the JVM alive.
        // Comment out createDeadlock() above and uncomment below:
        // fixedLockOrder();
    }
}