import java.util.*;

public class Practical12_WordFrequency {

    public static void main(String[] args) {

        String paragraph =
                "java is easy and java is powerful and java is popular";

        String[] words = paragraph.toLowerCase().split("\\s+");

        HashMap<String, Integer> frequency = new HashMap<>();

        for (String word : words) {
            frequency.put(word, frequency.getOrDefault(word, 0) + 1);
        }

        List<Map.Entry<String, Integer>> list =
                new ArrayList<>(frequency.entrySet());

        list.sort(new Comparator<Map.Entry<String, Integer>>() {

            @Override
            public int compare(
                    Map.Entry<String, Integer> a,
                    Map.Entry<String, Integer> b) {

                return Integer.compare(b.getValue(), a.getValue());
            }
        });

        System.out.println("Words sorted by frequency:");

        for (Map.Entry<String, Integer> entry : list) {
            System.out.println(
                    entry.getKey() + " : " + entry.getValue());
        }
    }
}