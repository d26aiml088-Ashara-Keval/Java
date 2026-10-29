package Lab_13;

public class BoundedStats {
    public static void main(String[] args) {
        Integer[] integers = {10, 20, 30};
        Double[] doubles = {2.5, 3.5, 4.0};
        Long[] longs = {100L, 200L, 300L};

        System.out.println("Integer average: " + average(integers));
        System.out.println("Double average: " + average(doubles));
        System.out.println("Long average: " + average(longs));
    }

    public static <N extends Number> double average(N[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Values must contain at least one number");
        }

        double sum = 0.0;
        for (N value : values) {
            if (value == null) {
                throw new IllegalArgumentException("Values must not contain null");
            }
            sum += value.doubleValue();
        }
        return sum / values.length;
    }
}
