import java.util.Scanner;

enum Coin {
    ONE(1),
    TWO(2),
    FIVE(5),
    TEN(10);

    private int value;

    Coin(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

public class VendingMachine {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int snackCost = 15;
        int total = 0;
        int value = 0;

        System.out.println("Snack Cost = " + snackCost);

        while (total < snackCost) {

            System.out.print("Enter Coin (ONE, TWO, FIVE, TEN): ");

            Coin coin = Coin.valueOf(sc.next().toUpperCase());

            switch (coin) {
                case ONE:
                    value = coin.getValue();
                    break;

                case TWO:
                    value = coin.getValue();
                    break;

                case FIVE:
                    value = coin.getValue();
                    break;

                case TEN:
                    value = coin.getValue();
                    break;
            }

            total += value;
            System.out.println("Total = " + total);
        }

        System.out.println("Paid");
        System.out.println("Change = " + (total - snackCost));

        sc.close();
    }
}