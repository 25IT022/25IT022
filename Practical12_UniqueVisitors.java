import java.util.*;

public class Practical12_UniqueVisitors {

    public static void main(String[] args) {

        List<String> loginNames = Arrays.asList(
                "Priyanshi",
                "Rutvi",
                "Riya",
                "Priyanshi",
                "Aarav",
                "Rutvi",
                "Neha",
                "Riya"
        );

        HashSet<String> uniqueUsers = new HashSet<>(loginNames);

        LinkedHashSet<String> firstSeenUsers =
                new LinkedHashSet<>(loginNames);

        System.out.println(
                "Total unique users: " + uniqueUsers.size());

        System.out.println("First three distinct names:");

        int count = 0;

        for (String name : firstSeenUsers) {

            System.out.println(name);

            count++;

            if (count == 3) {
                break;
            }
        }
    }
}