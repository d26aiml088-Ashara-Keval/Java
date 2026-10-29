package Lab_08;

import java.util.Scanner;

class DivideByZeroException extends Exception {
    DivideByZeroException(String message) {
        super(message);
    }
}

public class GuardedCalculator {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean success = false;

        while (!success) {
            try {
                System.out.print("Enter first number: ");
                double a = Double.parseDouble(sc.nextLine());

                System.out.print("Enter operator (+, -, *, /): ");
                String op = sc.nextLine();

                System.out.print("Enter second number: ");
                double b = Double.parseDouble(sc.nextLine());

                double result;

                switch (op) {
                    case "+" -> result = a + b;
                    case "-" -> result = a - b;
                    case "*" -> result = a * b;
                    case "/" -> {
                        if (b == 0) {
                            throw new DivideByZeroException(
                                "Cannot divide by zero.");
                        }
                        result = a / b;
                    }
                    default -> throw new IllegalArgumentException(
                        "Invalid operator.");
                }

                System.out.println("Result: " + result);
                success = true;

            } catch (NumberFormatException e) {
                System.out.println("Error: Enter valid numbers.");
            } catch (DivideByZeroException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } finally {
                System.out.println("Calculation attempt logged.");
            }
        }

        sc.close();
    }
}
