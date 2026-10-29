package util;

import model.Account;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class StatePersister {
    private StatePersister() {
    }

    public static void save(Account[] accounts, Path file) throws IOException {
        Path parent = file.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(file))) {
            output.writeObject(accounts);
        }
    }

    public static Account[] load(Path file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(file))) {
            Object state = input.readObject();
            if (!(state instanceof Account[] accounts)) {
                throw new InvalidObjectException("Saved state does not contain an Account array");
            }
            return accounts;
        }
    }
}
