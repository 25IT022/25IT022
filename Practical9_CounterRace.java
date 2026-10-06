class Counter {

    int count = 0;

    // Without synchronization
    void incrementWithoutSync() {
        count++;
    }

    // With synchronization
    synchronized void incrementWithSync() {
        count++;
    }
}

public class Practical9_CounterRace {

    static final int THREADS = 10;
    static final int INCREMENTS = 100000;

    public static void main(String[] args) throws InterruptedException {

        // -------- WITHOUT SYNCHRONIZATION --------

        Counter c1 = new Counter();
        Thread[] threads1 = new Thread[THREADS];

        for (int i = 0; i < THREADS; i++) {

            threads1[i] = new Thread(() -> {

                for (int j = 0; j < INCREMENTS; j++) {
                    c1.incrementWithoutSync();
                }

            });

            threads1[i].start();
        }

        for (Thread t : threads1) {
            t.join();
        }

        System.out.println("Without Synchronization: " + c1.count);
        System.out.println("Expected: " + (THREADS * INCREMENTS));


        // -------- WITH SYNCHRONIZATION --------

        Counter c2 = new Counter();
        Thread[] threads2 = new Thread[THREADS];

        for (int i = 0; i < THREADS; i++) {

            threads2[i] = new Thread(() -> {

                for (int j = 0; j < INCREMENTS; j++) {
                    c2.incrementWithSync();
                }

            });

            threads2[i].start();
        }

        for (Thread t : threads2) {
            t.join();
        }

        System.out.println("\nWith Synchronization: " + c2.count);
        System.out.println("Expected: " + (THREADS * INCREMENTS));
    }
}