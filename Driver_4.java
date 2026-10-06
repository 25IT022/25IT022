import java.util.Scanner;

public class Driver_4 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String[] s = {
            "10:05 alice Hello there",
            "1:50 Bob Hi",
            "2:45 charlie "
        };

        System.out.print("enter keyword: ");
        String keyword = sc.nextLine();

        int count = 0;
        StringBuilder report = new StringBuilder();

        for (int i = 0; i < s.length; i++) {

            String parts[] = s[i].split(" ", 3);

            if (parts.length < 3)
                continue;

            String time = parts[0];
            String user = parts[1];
            String message = parts[2];

            if (message.toLowerCase().contains(keyword.toLowerCase())) {

                count++;

                report.append(time)
                      .append(" ")
                      .append(user)
                      .append(": ")
                      .append(message)
                      .append("\n");
            }
        }

        System.out.println("Matches: " + count);
        System.out.println(report);
    }
}