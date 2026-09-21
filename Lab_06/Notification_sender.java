
@FunctionalInterface
interface Notifier {
    void send(String message);
}

// Marker interface
interface Urgent {
}

public class Notification_sender {

    public static void main(String[] args) {

        // Email sender
        Notifier email = message -> {
            System.out.println("Email: " + message);
        };

        // SMS sender
        Notifier sms = message -> {
            System.out.println("SMS: " + message);
        };

        // Store both in an array
        Notifier[] senders = {email, sms};

        // Message to broadcast
        String message = "Hello! This is an important notification.";

        // Broadcast
        for (Notifier sender : senders) {
            sender.send(message);
        }
    }
}