package Lab_08;

import java.util.*;

class OutOfStockException extends Exception {
    private int shortfall;

    OutOfStockException(int shortfall) {
        super("Out of stock. Shortfall: " + shortfall);
        this.shortfall = shortfall;
    }

    int getShortfall() {
        return shortfall;
    }
}

class InvalidQuantityException extends Exception {
    InvalidQuantityException(String message) {
        super(message);
    }
}

class Warehouse {
    private Map<String, Integer> stock = new HashMap<>();

    Warehouse() {
        stock.put("Pen", 10);
        stock.put("Book", 5);
        stock.put("Pencil", 20);
    }

    void issue(String item, int qty)
            throws OutOfStockException, InvalidQuantityException {

        if (qty <= 0) {
            throw new InvalidQuantityException(
                "Quantity must be greater than zero.");
        }

        int available = stock.getOrDefault(item, 0);

        if (qty > available) {
            throw new OutOfStockException(qty - available);
        }

        stock.put(item, available - qty);
        System.out.println("Issued " + qty + " " + item);
    }
}

public class StockIssue {
    public static void main(String[] args) {
        Warehouse w = new Warehouse();

        String[][] requests = {
            {"Pen", "4"},
            {"Book", "8"},
            {"Pencil", "0"},
            {"Pen", "3"},
            {"Notebook", "2"}
        };

        for (String[] request : requests) {
            try {
                String item = request[0];
                int qty = Integer.parseInt(request[1]);

                w.issue(item, qty);

            } catch (OutOfStockException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (InvalidQuantityException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid quantity format.");
            }
        }

        System.out.println("All requests processed.");
    }
}