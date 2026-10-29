package Lab_13;

public class GenericContainers {
    public static void main(String[] args) {
        Box<String> message = new Box<>("Hello, generics");
        message.set("Type-safe value");
        String text = message.get();

        Box<Integer> count = new Box<>(42);
        Integer number = count.get();

        Pair<String, Integer> entry = new Pair<>("items", 3);
        String key = entry.getFirst();
        Integer value = entry.getSecond();

        String[] names = {"Asha", "Mateo", "Zoe"};
        Integer[] numbers = {10, 20, 30};

        System.out.println("String box: " + text);
        System.out.println("Integer box: " + number);
        System.out.println("Pair: " + key + " = " + value);
        System.out.println("First name: " + first(names));
        System.out.println("First number: " + first(numbers));
    }

    public static <T> T first(T[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Array must contain at least one element");
        }
        return values[0];
    }

    private static class Box<T> {
        private T value;

        private Box(T value) {
            this.value = value;
        }

        private T get() {
            return value;
        }

        private void set(T value) {
            this.value = value;
        }
    }

    private static class Pair<A, B> {
        private final A first;
        private final B second;

        private Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        private A getFirst() {
            return first;
        }

        private B getSecond() {
            return second;
        }
    }
}
