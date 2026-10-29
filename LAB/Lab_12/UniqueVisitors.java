package Lab_12;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class UniqueVisitors {
    public static void main(String[] args) {
        List<String> loginNames = Arrays.asList(
                "mira", "noah", "mira", "olivia", "liam", "noah", "ava");
        Set<String> uniqueUsers = new HashSet<>(loginNames);
        Set<String> firstSeenOrder = new LinkedHashSet<>(loginNames);

        System.out.println("Unique users: " + uniqueUsers.size());
        System.out.println("First three distinct names:");
        firstSeenOrder.stream()
                .limit(3)
                .forEach(name -> System.out.println("  " + name));
    }
}
