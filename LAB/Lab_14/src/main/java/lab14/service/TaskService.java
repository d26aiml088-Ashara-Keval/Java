package lab14.service;

import lab14.model.Task;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TaskService {
    private final List<Task> tasks = new ArrayList<>();

    public Task addTask(String title) {
        Task task = new Task(title);
        tasks.add(task);
        return task;
    }

    public int getTaskCount() {
        return tasks.size();
    }

    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }

    public int performAction(String action, String taskTitle) {
        if ("add".equalsIgnoreCase(action)) {
            addTask(taskTitle);
            return getTaskCount();
        }
        if ("count".equalsIgnoreCase(action)) {
            return getTaskCount();
        }
        throw new IllegalArgumentException("Unknown action: " + action);
    }

    public void save(Path file) throws IOException {
        Path parent = file.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(file))) {
            output.writeObject(new ArrayList<>(tasks));
        }
    }

    public void load(Path file) throws IOException, ClassNotFoundException {
        List<Task> loadedTasks;
        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(file))) {
            Object value = input.readObject();
            if (!(value instanceof ArrayList<?> loadedValues)) {
                throw new IOException("Saved data does not contain a task list");
            }

            loadedTasks = new ArrayList<>(loadedValues.size());
            for (Object loadedValue : loadedValues) {
                if (!(loadedValue instanceof Task task)) {
                    throw new IOException("Saved data contains an invalid task");
                }
                loadedTasks.add(task);
            }
        }
        tasks.clear();
        tasks.addAll(loadedTasks);
    }
}
