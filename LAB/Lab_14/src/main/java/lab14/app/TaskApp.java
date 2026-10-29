package lab14.app;

import lab14.model.Task;
import lab14.service.TaskService;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class TaskApp {
    private static final Path DEFAULT_DATA_FILE = Paths.get("tasks.ser");

    private TaskApp() {
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Path dataFile = args.length > 0 ? Paths.get(args[0]) : DEFAULT_DATA_FILE;
        TaskService service = new TaskService();

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                printMenu();
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1" -> {
                        System.out.print("Task title: ");
                        int count = service.performAction("add", scanner.nextLine());
                        System.out.println("Task added. Total tasks: " + count);
                    }
                    case "2" -> {
                        if (service.getTasks().isEmpty()) {
                            System.out.println("No tasks yet.");
                        } else {
                            for (int i = 0; i < service.getTasks().size(); i++) {
                                System.out.printf("%d. %s%n", i + 1,
                                        service.getTasks().get(i).getTitle());
                            }
                        }
                    }
                    case "3" -> {
                        service.save(dataFile);
                        System.out.println("Saved " + service.getTaskCount()
                                + " task(s) to " + dataFile.toAbsolutePath());
                    }
                    case "4" -> {
                        service.load(dataFile);
                        System.out.println("Loaded " + service.getTaskCount()
                                + " task(s) from " + dataFile.toAbsolutePath());
                    }
                    case "5" -> running = false;
                    default -> throw new IllegalArgumentException("Invalid menu action: " + choice);
                }
            }
        }
        System.out.println("Goodbye.");
    }

    private static void printMenu() {
        System.out.println("\nTask menu");
        System.out.println("1. Add task");
        System.out.println("2. List tasks");
        System.out.println("3. Save tasks");
        System.out.println("4. Load tasks");
        System.out.println("5. Exit");
        System.out.print("Choose an option: ");
    }
}
