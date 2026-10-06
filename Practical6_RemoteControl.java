interface Switchable {

    void on();

    void off();

    default void toggle() {
        System.out.println("Toggle operation performed");
    }
}

class Fan implements Switchable {

    @Override
    public void on() {
        System.out.println("Fan is ON");
    }

    @Override
    public void off() {
        System.out.println("Fan is OFF");
    }
}

class Light implements Switchable {

    @Override
    public void on() {
        System.out.println("Light is ON");
    }

    @Override
    public void off() {
        System.out.println("Light is OFF");
    }
}

@FunctionalInterface
interface SwitchPermission {
    boolean canSwitchOn(Switchable device, int hour);
}

public class Practical6_RemoteControl {

    public static void main(String[] args) {

        Fan fan = new Fan();
        Light light = new Light();

        Switchable[] devices = {fan, light};

        System.out.println("===== TOGGLE DEVICES =====");

        for (Switchable device : devices) {
            device.toggle();
        }

        System.out.println("\n===== TURN ON =====");

        fan.on();
        light.on();

        System.out.println("\n===== TURN OFF =====");

        fan.off();
        light.off();

        System.out.println("\n===== ANONYMOUS CLASS =====");

        SwitchPermission permission1 = new SwitchPermission() {

            public boolean canSwitchOn(Switchable device, int hour) {
                return hour >= 8 && hour <= 22;
            }
        };

        int hour1 = 10;

        System.out.println(
            "Can Fan switch ON at " + hour1 + ":00? "
            + permission1.canSwitchOn(fan, hour1)
        );

        System.out.println("\n===== LAMBDA =====");

        SwitchPermission permission2 =
            (device, hour) -> hour >= 8 && hour <= 22;

        int hour2 = 23;

        System.out.println(
            "Can Light switch ON at " + hour2 + ":00? "
            + permission2.canSwitchOn(light, hour2)
        );
    }
}