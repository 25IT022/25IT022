import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Practical10_ThreadPool {

    public static void main(String[] args) {

        ExecutorService pool = Executors.newFixedThreadPool(3);

        for (int i = 1; i <= 10; i++) {

            final int taskId = i;

            pool.submit(() -> {

                String threadName = Thread.currentThread().getName();

                System.out.println(
                    "Task " + taskId + " is running on " + threadName
                );

                try {
                    Thread.sleep(500);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                System.out.println(
                    "Task " + taskId + " completed by " + threadName
                );
            });
        }

        pool.shutdown();

        try {

            if (pool.awaitTermination(10, TimeUnit.SECONDS)) {

                System.out.println("All tasks completed.");

            } else {

                System.out.println("Some tasks did not complete.");
            }

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.out.println("Main thread was interrupted.");
        }
    }
}