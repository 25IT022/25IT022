package discount;

import java.util.*;

public class Practical6_DiscountEngine {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        List<Double> prices = new ArrayList<>();

        System.out.print("Enter number of products: ");
        int n = sc.nextInt();

        for (int i = 0; i < n; i++) {
            System.out.print("Enter price " + (i + 1) + ": ");
            prices.add(sc.nextDouble());
        }

        System.out.println("\nChoose Discount Rule:");
        System.out.println("1. 10% Discount");
        System.out.println("2. 20% Discount");
        System.out.println("3. Flat Rs.50 Discount");

        System.out.print("Enter choice: ");
        int choice = sc.nextInt();

        DiscountRule rule;

        switch (choice) {

            case 1:
                rule = price -> price - (price * 0.10);
                break;

            case 2:
                rule = price -> price - (price * 0.20);
                break;

            case 3:
                rule = price -> Math.max(0, price - 50);
                break;

            default:
                System.out.println("Invalid choice!");
                sc.close();
                return;
        }

        System.out.println("\nOriginal Price -> Discounted Price");

        for (double price : prices) {

            double discountedPrice = rule.apply(price);

            System.out.printf("%.2f -> %.2f%n",
                    price, discountedPrice);
        }

        sc.close();
    }
}