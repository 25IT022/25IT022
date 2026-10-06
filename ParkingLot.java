import java.util.Scanner;

public class ParkingLot {

    private int twoWheelers;
    private int fourWheelers;

    private final int twoCap;
    private final int fourCap;

    private static long revenue = 0;

    // Constructor
    public ParkingLot(int twoCap, int fourCap) {
        this.twoCap = twoCap;
        this.fourCap = fourCap;
    }

    // Park vehicle
    public void park(String type) {

        switch (type.toLowerCase()) {

            case "two":
                if (twoWheelers < twoCap) {
                    twoWheelers++;
                    revenue += 20;
                    System.out.println("Two-wheeler parked.");
                } else {
                    System.out.println("Two-wheeler section Full!");
                }
                break;

            case "four":
                if (fourWheelers < fourCap) {
                    fourWheelers++;
                    revenue += 40;
                    System.out.println("Four-wheeler parked.");
                } else {
                    System.out.println("Four-wheeler section Full!");
                }
                break;

            default:
                System.out.println("Invalid vehicle type.");
        }
    }

    // Leave vehicle
    public void leave(String type) {

        switch (type.toLowerCase()) {

            case "two":
                if (twoWheelers > 0) {
                    twoWheelers--;
                    System.out.println("Two-wheeler left.");
                } else {
                    System.out.println("No two-wheelers parked.");
                }
                break;

            case "four":
                if (fourWheelers > 0) {
                    fourWheelers--;
                    System.out.println("Four-wheeler left.");
                } else {
                    System.out.println("No four-wheelers parked.");
                }
                break;

            default:
                System.out.println("Invalid vehicle type.");
        }
    }

    // Display final status
    public void displayStatus() {
        System.out.println("\nFinal Occupancy");
        System.out.println("Two-wheelers : " + twoWheelers);
        System.out.println("Four-wheelers: " + fourWheelers);
        System.out.println("Revenue       : ₹" + revenue);
    }

    public static void main(String[] args) {

        ParkingLot p = new ParkingLot(2, 2);

        p.park("two");
        p.park("two");
        p.park("two");      // Full

        p.park("four");
        p.park("four");
        p.park("four");     // Full

        p.leave("two");
        p.leave("four");

        p.park("two");
        p.park("four");

        p.displayStatus();
    }
}