import java.util.*;

class OutOfStockException extends Exception {

    private int shortfall;

    public OutOfStockException(String message, int shortfall) {
        super(message);
        this.shortfall = shortfall;
    }

    public int getShortfall() {
        return shortfall;
    }
}

class InvalidQuantityException extends Exception {

    public InvalidQuantityException(String message) {
        super(message);
    }
}

class Warehouse {

    private Map<String, Integer> stock = new HashMap<>();

    public Warehouse() {
        stock.put("Laptop", 5);
        stock.put("Mouse", 10);
        stock.put("Keyboard", 3);
    }

    public void issue(String item, int qty)
            throws OutOfStockException, InvalidQuantityException {

        if (qty <= 0) {
            throw new InvalidQuantityException(
                    "Quantity must be greater than 0."
            );
        }

        int available = stock.getOrDefault(item, 0);

        if (qty > available) {

            int shortfall = qty - available;

            throw new OutOfStockException(
                    "Not enough stock for " + item,
                    shortfall
            );
        }

        stock.put(item, available - qty);

        System.out.println(
                "Issued " + qty + " " + item + "(s) successfully."
        );
    }
}

public class Practical8_StockIssue {

    public static void main(String[] args) {

        Warehouse warehouse = new Warehouse();

        String[] items = {
            "Laptop",
            "Mouse",
            "Keyboard",
            "Laptop"
        };

        int[] quantities = {
            2,
            15,
            0,
            10
        };

        for (int i = 0; i < items.length; i++) {

            try {

                System.out.println(
                        "Request: " + items[i] +
                        " - Quantity: " + quantities[i]
                );

                warehouse.issue(items[i], quantities[i]);

            } catch (OutOfStockException e) {

                System.out.println(
                        "Out of stock: " + e.getMessage()
                );

                System.out.println(
                        "Shortfall: " + e.getShortfall()
                );

            } catch (InvalidQuantityException e) {

                System.out.println(
                        "Invalid quantity: " + e.getMessage()
                );

            }

            System.out.println();
        }

        System.out.println("All requests processed.");
    }
}