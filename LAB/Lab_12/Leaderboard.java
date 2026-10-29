package Lab_12;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Leaderboard {
    private static final int TOP_N = 3;

    public static void main(String[] args) {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Ava", 940);
        scores.put("Noah", 875);
        scores.put("Mira", 940);
        scores.put("Liam", 810);
        scores.put("Zoe", 875);

        System.out.println("Top " + TOP_N + " players (score descending, name ascending for ties):");
        scores.entrySet().stream()
                .sorted(Comparator
                        .<Map.Entry<String, Integer>>comparingInt(Map.Entry::getValue)
                        .reversed()
                        .thenComparing(Map.Entry::getKey))
                .limit(TOP_N)
                .forEach(entry -> System.out.printf("%s: %d%n",
                        entry.getKey(), entry.getValue()));
    }
}
