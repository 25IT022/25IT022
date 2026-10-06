import java.util.Scanner;

public class Thermostat {
    private String location;               // where this thermostat is
    private int temperature;               // current temperature (Celsius)

    private static final int MIN = 16;     // shared limits for every thermostat
    private static final int MAX = 30;
    private static int activeCount = 0;    // how many Thermostat objects exist

    /** Full constructor: keep startTemp only if it is within the safe range. */
    public Thermostat(String location, int startTemp) {
        this.location = location;
        this.temperature = (startTemp >= MIN && startTemp <= MAX) ? startTemp : 22;
        activeCount++;                     // one more thermostat now exists
    }

    /** Convenience constructor: chains to the full one with a default of 22. */
    public Thermostat(String location) {
        this(location, 22);
    }

    /** Raise by 1, but never above MAX. */
    public void raise() {
        if (temperature < MAX) temperature++;
        else System.out.println("  Already at maximum (" + MAX + ")");
    }

    /** Lower by 1, but never below MIN. */
    public void lower() {
        if (temperature > MIN) temperature--;
        else System.out.println("  Already at minimum (" + MIN + ")");
    }

    public int getTemperature() { return temperature; }   // getter only, NO setter
    public String getLocation() { return location; }
    public static int getActiveCount() { return activeCount; }

    public static void main(String[] args) {
        Thermostat living  = new Thermostat("Living Room", 24);
        Thermostat bedroom = new Thermostat("Bedroom");     // defaults to 22

        System.out.println("Raising " + living.getLocation() + ":");
        for (int i = 0; i < 10; i++) {          // try to raise ten times
            living.raise();
            System.out.println("  temp = " + living.getTemperature());
        }

        System.out.println("Lowering " + bedroom.getLocation() + ":");
        for (int i = 0; i < 20; i++) {          // try to lower twenty times
            bedroom.lower();
            System.out.println("  temp = " + bedroom.getTemperature());
        }

        System.out.println("Active thermostats: " + Thermostat.getActiveCount());
    }
}
