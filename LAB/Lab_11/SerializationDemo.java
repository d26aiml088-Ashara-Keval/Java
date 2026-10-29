package Lab_11;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class SerializationDemo {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Path folder = Paths.get(args.length > 0 ? args[0] : "Lab_11")
                .toAbsolutePath().normalize();
        Files.createDirectories(folder);
        Path file = folder.resolve("records.ser");

        PersonRecord[] original = {
                new PersonRecord("Asha", 21, "session-A"),
                new PersonRecord("Mateo", 24, "session-B")
        };

        try (ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(file))) {
            output.writeObject(original);
        }

        PersonRecord[] restored;
        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(file))) {
            restored = (PersonRecord[]) input.readObject();
        }

        boolean valuesSurvived = restored.length == original.length;
        for (int i = 0; valuesSurvived && i < original.length; i++) {
            valuesSurvived = original[i].hasSameSavedValues(restored[i]);
        }

        System.out.println("Serialization:");
        for (PersonRecord record : restored) {
            System.out.println("  " + record);
        }
        System.out.println("Saved values survived: " + valuesSurvived);
        System.out.println("Transient session token restored as null: "
                + (restored.length > 0 && restored[0].sessionToken == null));
    }

    private static class PersonRecord implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String name;
        private final int age;
        private transient String sessionToken;

        private PersonRecord(String name, int age, String sessionToken) {
            this.name = name;
            this.age = age;
            this.sessionToken = sessionToken;
        }

        private boolean hasSameSavedValues(PersonRecord other) {
            return Objects.equals(name, other.name) && age == other.age;
        }

        @Override
        public String toString() {
            return "PersonRecord{name='" + name + "', age=" + age
                    + ", sessionToken=" + sessionToken + "}";
        }
    }
}
