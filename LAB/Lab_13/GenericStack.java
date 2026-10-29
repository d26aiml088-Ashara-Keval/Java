package Lab_13;

import java.util.NoSuchElementException;

public class GenericStack {
    public static void main(String[] args) {
        ArrayStack<String> words = new ArrayStack<>(3);
        words.push("first");
        words.push("second");
        System.out.println("Popped word: " + words.pop());

        ArrayStack<Integer> numbers = new ArrayStack<>(2);
        numbers.push(10);
        numbers.push(20);
        System.out.println("Top number: " + numbers.peek());
        System.out.println("Popped number: " + numbers.pop());

        while (!words.isEmpty()) {
            System.out.println("Popped word: " + words.pop());
        }
        try {
            words.pop();
        } catch (NoSuchElementException exception) {
            System.out.println("Empty stack: " + exception.getMessage());
        }
    }

    private static class ArrayStack<T> {
        private final Object[] elements;
        private int size;

        private ArrayStack(int capacity) {
            if (capacity <= 0) {
                throw new IllegalArgumentException("Capacity must be positive");
            }
            elements = new Object[capacity];
        }

        private void push(T value) {
            if (size == elements.length) {
                throw new IllegalStateException("Stack is full");
            }
            elements[size++] = value;
        }

        @SuppressWarnings("unchecked")
        private T pop() {
            if (isEmpty()) {
                throw new NoSuchElementException("Cannot pop from an empty stack");
            }
            int top = --size;
            T value = (T) elements[top];
            elements[top] = null;
            return value;
        }

        @SuppressWarnings("unchecked")
        private T peek() {
            if (isEmpty()) {
                throw new NoSuchElementException("Cannot peek at an empty stack");
            }
            return (T) elements[size - 1];
        }

        private boolean isEmpty() {
            return size == 0;
        }
    }
}
