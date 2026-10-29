public class CinemaShow {

    private String title;
    private int seatsAvailable;
    private final int capacity;

    private static int totalBooked = 0;

    public CinemaShow(String title, int capacity) {
        this.title = title;
        this.capacity = capacity;
        this.seatsAvailable = capacity;
    }

    public CinemaShow(String title) {
        this(title, 100);
    }

    public boolean book(int n) {
        if (n <= seatsAvailable) {
            seatsAvailable -= n;
            totalBooked += n;
            return true;
        } else {
            return false;
        }
    }

    public void cancel(int n) {
        seatsAvailable += n;

        if (seatsAvailable > capacity) {
            seatsAvailable = capacity;
        }
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public static int getTotalBooked() {
        return totalBooked;
    }

    public static void main(String[] args) {

        CinemaShow show1 = new CinemaShow("Avengers", 50);

        System.out.println("Booking 20 seats: " + show1.book(20));
        System.out.println("Seats Available: " + show1.getSeatsAvailable());

        System.out.println();

        System.out.println("Booking 25 seats: " + show1.book(25));
        System.out.println("Seats Available: " + show1.getSeatsAvailable());

        System.out.println();

        System.out.println("Booking 10 seats: " + show1.book(10));
        System.out.println("Seats Available: " + show1.getSeatsAvailable());

        System.out.println();

        show1.cancel(15);
        System.out.println("Cancelled 15 seats");
        System.out.println("Seats Available: " + show1.getSeatsAvailable());

        System.out.println();

        System.out.println("Booking 10 seats: " + show1.book(10));
        System.out.println("Seats Available: " + show1.getSeatsAvailable());

        System.out.println();

        show1.cancel(100);
        System.out.println("Cancelled 100 seats");
        System.out.println("Seats Available: " + show1.getSeatsAvailable());

        System.out.println();

        System.out.println("Total Booked Seats: " + CinemaShow.getTotalBooked());
    }
}