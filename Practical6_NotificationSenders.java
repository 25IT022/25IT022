@FunctionalInterface
interface Notifier {
    void send(String message);
}

interface Urgent {
}

public class Practical6_NotificationSenders {

    public static void main(String[] args) {

        Notifier email = message ->
            System.out.println("Email: " + message);

        Notifier sms = message ->
            System.out.println("SMS: " + message);

        class UrgentEmail implements Notifier, Urgent {

            @Override
            public void send(String message) {
                System.out.println("Urgent Email: " + message);
            }
        }

        Notifier urgentEmail = new UrgentEmail();

        Notifier[] senders = {
            email,
            sms,
            urgentEmail
        };

        String message = "System maintenance at 10 PM";

        System.out.println("===== BROADCAST =====");

        for (Notifier sender : senders) {

            sender.send(message);

            if (sender instanceof Urgent) {
                sender.send(message);
            }
        }
    }
}