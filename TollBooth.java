import java.util.Scanner;

public class TollBooth {

    // A record is a compact, immutable data holder. This one line gives us a
    // constructor, the accessors number()/type(), plus equals/hashCode/toString.
    record Vehicle(String number, String type) { }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int totalToll = 0;
        int bikes = 0, cars = 0, trucks = 0;      // per-type counters

        System.out.println("Enter vehicles. Type 'done' for the number to stop.");

        while (true) {
            System.out.print("Vehicle number (or 'done'): ");
            String number = sc.next();
            if (number.equalsIgnoreCase("done")) break;

            System.out.print("Type (bike/car/truck): ");
            String type = sc.next().toLowerCase();

            Vehicle v = new Vehicle(number, type);   // build the record

            // Block form of a switch expression: it runs statements and
            // returns a value with 'yield'.
            int toll = switch (v.type()) {
                case "bike"  -> { bikes++;  yield 20;  }
                case "car"   -> { cars++;   yield 50;  }
                case "truck" -> { trucks++; yield 150; }
                default      -> 0;                       // unknown type pays 0
            };

            totalToll += toll;
            System.out.println("  " + v.number() + " (" + v.type() + ") pays " + toll);
        }

        System.out.println("Total toll collected: " + totalToll);

        // Most frequent type by comparing counters.
        String mostFrequent;
        if (bikes >= cars && bikes >= trucks)      mostFrequent = "bike";
        else if (cars >= bikes && cars >= trucks)  mostFrequent = "car";
        else                                       mostFrequent = "truck";
        System.out.println("Most frequent: " + mostFrequent);
        sc.close();
    }
}