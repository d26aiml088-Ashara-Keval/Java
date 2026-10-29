package util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public final class TransactionLog {
    private TransactionLog() {
    }

    public static synchronized void append(Path file, String line) throws IOException {
        appendAll(file, List.of(line));
    }

    public static synchronized void appendAll(Path file, List<String> lines) throws IOException {
        if (file == null || lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("Log file and entries are required");
        }
        for (String line : lines) {
            if (line == null || line.indexOf('\n') >= 0 || line.indexOf('\r') >= 0) {
                throw new IllegalArgumentException("Log entries must each occupy one line");
            }
        }
        String content = String.join(System.lineSeparator(), lines) + System.lineSeparator();
        Path parent = file.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(file, content, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}
