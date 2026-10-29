package Lab_12;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class WordFrequency {
    public static void main(String[] args) {
        String paragraph = "Java collections make it easy to count words. "
                + "Java maps count words, and collections help organize them.";
        Map<String, Integer> frequencies = new HashMap<>();

        for (String word : paragraph.toLowerCase().split("[^a-z0-9]+")) {
            if (!word.isEmpty()) {
                frequencies.merge(word, 1, Integer::sum);
            }
        }

        System.out.println("Words sorted by frequency:");
        frequencies.entrySet().stream()
                .sorted(Comparator
                        .<Map.Entry<String, Integer>>comparingInt(Map.Entry::getValue)
                        .reversed()
                        .thenComparing(Map.Entry::getKey))
                .forEach(entry -> System.out.printf("%s: %d%n",
                        entry.getKey(), entry.getValue()));
    }
}
