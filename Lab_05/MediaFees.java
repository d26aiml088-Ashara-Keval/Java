package Lab_05;

abstract class Media {
    String title;
    int overdueDays;

    Media(String title, int overdueDays) {
        this.title = title;
        this.overdueDays = Math.max(0, overdueDays);
    }

    abstract double lateFee();
}

class Book extends Media {
    Book(String title, int days) {
        super(title, days);
    }

    double lateFee() {
        return overdueDays * 2;
    }
}

class DVD extends Media {
    DVD(String title, int days) {
        super(title, days);
    }

    double lateFee() {
        return overdueDays * 5;
    }
}

class Magazine extends Media {
    Magazine(String title, int days) {
        super(title, days);
    }

    double lateFee() {
        return overdueDays * 3;
    }
}

public class MediaFees {
    public static void main(String[] args) {
        Media[] returnedItems = {
            new Book("Java Basics", 4),
            new DVD("Inception", 3),
            new Magazine("Science Today", 2)
        };

        double totalFees = 0;

        for (Media m : returnedItems) {
            double fee = m.lateFee();

            System.out.println(
                m.title + " late fee: Rs. " + fee);

            totalFees += fee;
        }

        System.out.println(
            "Total late fees: Rs. " + totalFees);
    }
}