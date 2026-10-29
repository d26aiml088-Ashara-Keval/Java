package Lab_11;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FolderReport {
    private static final String REPORT_NAME = "folder-report.txt";

    public static void main(String[] args) throws IOException {
        Path folder = Paths.get(args.length > 0 ? args[0] : "Lab_11")
                .toAbsolutePath().normalize();
        Files.createDirectories(folder);
        Path report = folder.resolve(REPORT_NAME);
        List<String> entries = new ArrayList<>();

        Files.walkFileTree(folder, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
                if (!file.equals(report)) {
                    entries.add(String.format("%s | %d bytes | modified %s",
                            folder.relativize(file), attributes.size(),
                            attributes.lastModifiedTime()));
                }
                return FileVisitResult.CONTINUE;
            }
        });

        entries.sort(String::compareTo);
        List<String> reportLines = new ArrayList<>();
        reportLines.add("Files under " + folder);
        reportLines.add("Relative file name | size | last modified");
        reportLines.addAll(entries);
        reportLines.add("File count: " + entries.size());
        Files.write(report, reportLines, StandardCharsets.UTF_8);
        System.out.println("Folder report written to: " + report);
    }
}
