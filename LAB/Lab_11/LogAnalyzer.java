package Lab_11;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Locale;

public class LogAnalyzer {
    private static final String KEYWORD = "error";

    public static void main(String[] args) throws IOException {
        Path folder = Paths.get(args.length > 0 ? args[0] : "Lab_11")
                .toAbsolutePath().normalize().resolve("logs");
        Files.createDirectories(folder);
        createSampleLogs(folder);

        int totalLines = 0;
        int matchingLines = 0;
        System.out.println("Log analysis (keyword: \"" + KEYWORD + "\"):");
        try (java.util.stream.Stream<Path> paths = Files.list(folder)) {
            for (Path path : (Iterable<Path>) paths.filter(Files::isRegularFile)
                    .sorted()::iterator) {
                int fileLines = 0;
                int fileMatches = 0;
                try (BufferedReader reader =
                             Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        fileLines++;
                        if (line.toLowerCase(Locale.ROOT).contains(KEYWORD)) {
                            fileMatches++;
                        }
                    }
                }

                BasicFileAttributes attributes =
                        Files.readAttributes(path, BasicFileAttributes.class);
                totalLines += fileLines;
                matchingLines += fileMatches;
                System.out.printf("  %s: %d bytes, %d lines, %d matching lines%n",
                        path.getFileName(), attributes.size(), fileLines, fileMatches);
            }
        }
        System.out.println("Total lines: " + totalLines);
        System.out.println("Lines containing \"" + KEYWORD + "\": " + matchingLines);
    }

    private static void createSampleLogs(Path folder) throws IOException {
        Files.write(folder.resolve("application.log"), List.of(
                "Application started",
                "ERROR: could not connect to database",
                "User signed in"
        ), StandardCharsets.UTF_8);
        Files.write(folder.resolve("worker.log"), List.of(
                "Worker ready",
                "Retrying after error",
                "Job completed",
                "No errors reported"
        ), StandardCharsets.UTF_8);
        Files.write(folder.resolve("audit.log"), List.of(
                "Account created",
                "Password updated"
        ), StandardCharsets.UTF_8);
    }
}
