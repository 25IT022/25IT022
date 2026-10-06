public class Practical10_Deadlock {

    private static final Object lockA = new Object();
    private static final Object lockB = new Object();

    public static void main(String[] args)
            throws InterruptedException {

        System.out.println("DEADLOCK DEMONSTRATION");

        Thread thread1 = new Thread(() -> {

            synchronized (lockA) {

                System.out.println("Thread 1 locked A");

                try {
                    Thread.sleep(100);

                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();
                }

                synchronized (lockB) {

                    System.out.println("Thread 1 locked B");
                }
            }
        });

        Thread thread2 = new Thread(() -> {

            synchronized (lockB) {

                System.out.println("Thread 2 locked B");

                try {
                    Thread.sleep(100);

                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();
                }

                synchronized (lockA) {

                    System.out.println("Thread 2 locked A");
                }
            }
        });

        thread1.start();
        thread2.start();

        Thread.sleep(500);

        System.out.println(
            "Both threads may be waiting for each other."
        );

        System.out.println(
            "This demonstrates a deadlock."
        );

        System.out.println();

        System.out.println("DEADLOCK SOLUTION");

        System.out.println(
            "Use the same lock order for both threads."
        );

        Thread fixedThread1 = new Thread(() -> {

            synchronized (lockA) {

                System.out.println("Fixed Thread 1 locked A");

                synchronized (lockB) {

                    System.out.println("Fixed Thread 1 locked B");
                }
            }
        });

        Thread fixedThread2 = new Thread(() -> {

            synchronized (lockA) {

                System.out.println("Fixed Thread 2 locked A");

                synchronized (lockB) {

                    System.out.println("Fixed Thread 2 locked B");
                }
            }
        });

        fixedThread1.start();
        fixedThread2.start();

        fixedThread1.join();
        fixedThread2.join();

        System.out.println(
            "Both fixed threads completed successfully."
        );
    }
}