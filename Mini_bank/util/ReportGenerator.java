package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public final class ReportGenerator {
    private ReportGenerator() {
    }

    public static String generate(Path logsFolder, Path reportFile) throws IOException {
        long deposits = 0;
        long withdrawals = 0;
        List<String> details = new ArrayList<>();
        Files.createDirectories(logsFolder);

        try (DirectoryStream<Path> logFiles = Files.newDirectoryStream(logsFolder, "*.log")) {
            for (Path logFile : logFiles) {
                if (!Files.isRegularFile(logFile)) {
                    continue;
                }
                BasicFileAttributes attributes =
                        Files.readAttributes(logFile, BasicFileAttributes.class);
                details.add(String.format("%s | %d bytes | modified %s",
                        logFile.getFileName(), attributes.size(), attributes.lastModifiedTime()));

                try (BufferedReader reader =
                        Files.newBufferedReader(logFile, StandardCharsets.UTF_8)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] fields = line.split(",", -1);
                        if (fields.length != 2) {
                            throw new IOException("Malformed transaction in " + logFile);
                        }
                        long amount;
                        try {
                            amount = Long.parseLong(fields[1]);
                        } catch (NumberFormatException exception) {
                            throw new IOException("Invalid amount in " + logFile, exception);
                        }
                        if (amount < 0) {
                            throw new IOException("Negative amount in " + logFile);
                        }
                        try {
                            if ("DEPOSIT".equals(fields[0])) {
                                deposits = Math.addExact(deposits, amount);
                            } else if ("WITHDRAWAL".equals(fields[0])) {
                                withdrawals = Math.addExact(withdrawals, amount);
                            } else {
                                throw new IOException("Unknown transaction type in " + logFile);
                            }
                        } catch (ArithmeticException exception) {
                            throw new IOException("Transaction total overflow", exception);
                        }
                    }
                }
            }
        }

        long netMovement;
        try {
            netMovement = Math.subtractExact(deposits, withdrawals);
        } catch (ArithmeticException exception) {
            throw new IOException("Net transaction total overflow", exception);
        }

        StringBuilder report = new StringBuilder("End-of-day transaction summary\n")
                .append("Total deposits: ").append(deposits).append('\n')
                .append("Total withdrawals: ").append(withdrawals).append('\n')
                .append("Net movement: ").append(netMovement).append('\n')
                .append("Log files:\n");
        for (String detail : details) {
            report.append("  ").append(detail).append('\n');
        }

        Path parent = reportFile.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(reportFile, report.toString(), StandardCharsets.UTF_8);
        return report.toString();
    }
}
