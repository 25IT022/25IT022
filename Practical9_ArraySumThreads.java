import java.util.Random;

class SharedTotal {

    long total = 0;

    void add(long value) {
        total += value;
    }

    synchronized void addSynchronized(long value) {
        total += value;
    }
}

public class Practical9_ArraySumThreads {

    static final int SIZE = 1000000;
    static final int THREADS = 4;

    public static void main(String[] args) throws InterruptedException {

        int[] numbers = new int[SIZE];

        Random random = new Random(1);

        for (int i = 0; i < SIZE; i++) {
            numbers[i] = random.nextInt(100);
        }

        long expected = 0;

        for (int n : numbers) {
            expected += n;
        }

        System.out.println("Expected Sum: " + expected);


        // -------- WITHOUT SYNCHRONIZATION --------

        SharedTotal wrongTotal = new SharedTotal();

        Thread[] t1 = new Thread[THREADS];

        long start1 = System.nanoTime();

        int part = SIZE / THREADS;

        for (int i = 0; i < THREADS; i++) {

            final int start = i * part;
            final int end = (i == THREADS - 1) ? SIZE : start + part;

            t1[i] = new Thread(() -> {

                for (int j = start; j < end; j++) {
                    wrongTotal.add(numbers[j]);
                }

            });

            t1[i].start();
        }

        for (Thread t : t1) {
            t.join();
        }

        long end1 = System.nanoTime();

        System.out.println("\nWithout Synchronization:");
        System.out.println("Sum = " + wrongTotal.total);
        System.out.println("Time = " + (end1 - start1) + " ns");


        // -------- WITH SYNCHRONIZATION --------

        SharedTotal syncTotal = new SharedTotal();

        Thread[] t2 = new Thread[THREADS];

        long start2 = System.nanoTime();

        for (int i = 0; i < THREADS; i++) {

            final int start = i * part;
            final int end = (i == THREADS - 1) ? SIZE : start + part;

            t2[i] = new Thread(() -> {

                for (int j = start; j < end; j++) {
                    syncTotal.addSynchronized(numbers[j]);
                }

            });

            t2[i].start();
        }

        for (Thread t : t2) {
            t.join();
        }

        long end2 = System.nanoTime();

        System.out.println("\nWith Synchronization:");
        System.out.println("Sum = " + syncTotal.total);
        System.out.println("Time = " + (end2 - start2) + " ns");


        // -------- USING PARTIAL SUMS --------

        long[] partialSums = new long[THREADS];

        Thread[] t3 = new Thread[THREADS];

        long start3 = System.nanoTime();

        for (int i = 0; i < THREADS; i++) {

            final int index = i;
            final int start = i * part;
            final int end = (i == THREADS - 1) ? SIZE : start + part;

            t3[i] = new Thread(() -> {

                long localSum = 0;

                for (int j = start; j < end; j++) {
                    localSum += numbers[j];
                }

                partialSums[index] = localSum;

            });

            t3[i].start();
        }

        for (Thread t : t3) {
            t.join();
        }

        long finalSum = 0;

        for (long sum : partialSums) {
            finalSum += sum;
        }

        long end3 = System.nanoTime();

        System.out.println("\nUsing Partial Sums:");
        System.out.println("Sum = " + finalSum);
        System.out.println("Time = " + (end3 - start3) + " ns");
    }
}