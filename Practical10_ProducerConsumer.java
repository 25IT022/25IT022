import java.util.LinkedList;
import java.util.Queue;

class SharedBuffer {

    private Queue<Integer> buffer = new LinkedList<>();

    private final int capacity = 3;

    public synchronized void produce(int value)
            throws InterruptedException {

        while (buffer.size() == capacity) {
            wait();
        }

        buffer.add(value);

        System.out.println("Produced: " + value);

        notify();
    }

    public synchronized void consume()
            throws InterruptedException {

        while (buffer.isEmpty()) {
            wait();
        }

        int value = buffer.remove();

        System.out.println("Consumed: " + value);

        notify();
    }
}

public class Practical10_ProducerConsumer {

    public static void main(String[] args) {

        SharedBuffer buffer = new SharedBuffer();

        Thread producer = new Thread(new Runnable() {

            public void run() {

                try {

                    for (int i = 1; i <= 10; i++) {

                        buffer.produce(i);

                        Thread.sleep(100);
                    }

                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread consumer = new Thread(new Runnable() {

            public void run() {

                try {

                    for (int i = 1; i <= 10; i++) {

                        buffer.consume();

                        Thread.sleep(150);
                    }

                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();
                }
            }
        });

        producer.start();
        consumer.start();

        try {

            producer.join();
            consumer.join();

            System.out.println(
                "Production and consumption completed."
            );

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }
    }
}