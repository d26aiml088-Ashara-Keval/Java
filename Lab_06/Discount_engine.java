import java.util.Scanner;

public class Discount_engine {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        double[] prices = {1000, 2000, 500, 3000};

        System.out.println("Choose discount:");
        System.out.println("1. 10% discount");
        System.out.println("2. 20% discount");
        System.out.println("3. ₹100 discount");

        int choice = sc.nextInt();

        DiscountRule rule;

        if (choice == 1) {

            rule = price -> price * 0.90;

        } else if (choice == 2) {

            rule = price -> price * 0.80;

        } else if (choice == 3) {

            rule = price -> price - 100;

        } else {

            System.out.println("Invalid choice.");
            return;
        }

        System.out.println("\nDiscounted prices:");

        for (double price : prices) {
            System.out.println(rule.apply(price));
        }

        sc.close();
    }
}