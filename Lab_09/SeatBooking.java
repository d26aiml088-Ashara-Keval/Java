package Lab_09;
class Booking {
    int seatsLeft = 5;
    int successful = 0;

    void bookUnsafe() {
        if (seatsLeft > 0) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            seatsLeft--;
            successful++;
        }
    }

    synchronized void bookSafe() {
        if (seatsLeft > 0) {
            seatsLeft--;
            successful++;
        }
    }
}

public class SeatBooking{
    public static void main(String[] args)
            throws InterruptedException {
        Thread[] threads = new Thread[10];

        Booking unsafe = new Booking();

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(unsafe::bookUnsafe);
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println("Without synchronization:");
        System.out.println("Successful bookings: " + unsafe.successful);
        System.out.println("Seats left: " + unsafe.seatsLeft);

        Booking safe = new Booking();

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(safe::bookSafe);
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println("\nWith synchronization:");
        System.out.println("Successful bookings: " + safe.successful);
        System.out.println("Seats left: " + safe.seatsLeft);
    }
}