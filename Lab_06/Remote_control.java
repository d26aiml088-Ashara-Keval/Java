interface Switchable {
    void on();
    void off();

    default void toggle() {
        System.out.println("Toggling the device.");
    }
}

class Fan implements Switchable {
    @Override
    public void on() {
        System.out.println("Fan is turned ON.");
    }

    @Override
    public void off() {
        System.out.println("Fan is turned OFF.");
    }
}

class Light implements Switchable {
    @Override
    public void on() {
        System.out.println("Light is turned ON.");
    }

    @Override
    public void off() {
        System.out.println("Light is turned OFF.");
    }
}

// Functional interface
@FunctionalInterface
interface SwitchPolicy {
    boolean maySwitchOn(Switchable device, int hour);
}

public class Remote_control {
    public static void main(String[] args) {

        // Array of Switchable devices
        Switchable[] devices = {
            new Fan(),
            new Light()
        };

        // Toggle every device
        for (Switchable device : devices) {
            device.toggle();
        }

        // Anonymous class
        SwitchPolicy policy1 = new SwitchPolicy() {
            @Override
            public boolean maySwitchOn(Switchable device, int hour) {
                return hour >= 6 && hour <= 22;
            }
        };

        // Lambda
        SwitchPolicy policy2 = (device, hour) -> hour >= 8 && hour <= 20;

        // Testing anonymous class
        System.out.println("Anonymous class:");
        System.out.println(policy1.maySwitchOn(devices[0], 7));

        // Testing lambda
        System.out.println("Lambda:");
        System.out.println(policy2.maySwitchOn(devices[1], 7));
    }
}