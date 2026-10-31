import java.util.*;

public class Practical12_Leaderboard {

    public static void main(String[] args) {

        Map<String, Integer> scores = new HashMap<>();

        scores.put("Priyanshi", 950);
        scores.put("Rutvi", 880);
        scores.put("Riya", 950);
        scores.put("Aarav", 920);
        scores.put("Neha", 870);
        scores.put("Karan", 920);

        List<Map.Entry<String, Integer>> players =
                new ArrayList<>(scores.entrySet());

        players.sort(new Comparator<Map.Entry<String, Integer>>() {

            @Override
            public int compare(
                    Map.Entry<String, Integer> a,
                    Map.Entry<String, Integer> b) {

                int scoreComparison =
                        Integer.compare(b.getValue(), a.getValue());

                if (scoreComparison != 0) {
                    return scoreComparison;
                }

                return a.getKey().compareTo(b.getKey());
            }
        });

        int topN = 5;

        System.out.println("Top " + topN + " Players:");
        System.out.println("--------------------");

        int count = 0;

        for (Map.Entry<String, Integer> player : players) {

            System.out.println(
                    player.getKey() + " : " + player.getValue());

            count++;

            if (count == topN) {
                break;
            }
        }
    }
}