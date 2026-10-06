class BookingSystem {

    private int seatsLeft = 5;

    // Without synchronization
    boolean bookWithoutSync(String name) {

        if (seatsLeft > 0) {

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            seatsLeft--;

            System.out.println(name + " booked a seat.");

            return true;
        }

        return false;
    }

    // With synchronization
    synchronized boolean bookWithSync(String name) {

        if (seatsLeft > 0) {

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            seatsLeft--;

            System.out.println(name + " booked a seat.");

            return true;
        }

        return false;
    }

    int getSeatsLeft() {
        return seatsLeft;
    }
}

public class Practical9_SeatBookingRace {

    public static void main(String[] args) throws InterruptedException {

        // -------- WITHOUT SYNCHRONIZATION --------

        System.out.println("WITHOUT SYNCHRONIZATION");

        BookingSystem system1 = new BookingSystem();

        Thread[] threads1 = new Thread[10];

        for (int i = 0; i < 10; i++) {

            final int id = i + 1;

            threads1[i] = new Thread(() -> {
                system1.bookWithoutSync("User " + id);
            });

            threads1[i].start();
        }

        for (Thread t : threads1) {
            t.join();
        }

        System.out.println("Seats left: " + system1.getSeatsLeft());


        // -------- WITH SYNCHRONIZATION --------

        System.out.println("\nWITH SYNCHRONIZATION");

        BookingSystem system2 = new BookingSystem();

        Thread[] threads2 = new Thread[10];

        for (int i = 0; i < 10; i++) {

            final int id = i + 1;

            threads2[i] = new Thread(() -> {
                system2.bookWithSync("User " + id);
            });

            threads2[i].start();
        }

        for (Thread t : threads2) {
            t.join();
        }

        System.out.println("Seats left: " + system2.getSeatsLeft());
    }
}