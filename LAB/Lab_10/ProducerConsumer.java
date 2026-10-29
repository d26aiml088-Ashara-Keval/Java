package Lab_10;

import java.util.LinkedList;
import java.util.Queue;

class Buffer {
    private final Queue<Integer> queue = new LinkedList<>();
    private final int capacity = 3;

    synchronized void produce(int value)
            throws InterruptedException {
        while (queue.size() == capacity) {
            wait();
        }

        queue.add(value);
        System.out.println("Produced: " + value);

        notifyAll();
    }

    synchronized int consume()
            throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }

        int value = queue.remove();
        System.out.println("Consumed: " + value);

        notifyAll();
        return value;
    }
}

public class ProducerConsumer {
    public static void main(String[] args) {
        Buffer buffer = new Buffer();
        int items = 10;

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= items; i++) {
                    buffer.produce(i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= items; i++) {
                    buffer.consume();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("All items processed.");
    }
}
