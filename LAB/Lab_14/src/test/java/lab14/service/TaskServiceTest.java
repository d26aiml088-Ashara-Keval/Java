package lab14.service;

import lab14.model.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskServiceTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void addingTaskIncreasesCount() {
        TaskService service = new TaskService();

        service.addTask("Review Java");

        assertEquals(1, service.getTaskCount());
    }

    @Test
    void invalidActionThrows() {
        TaskService service = new TaskService();

        assertThrows(IllegalArgumentException.class,
                () -> service.performAction("delete", "Review Java"));
    }

    @Test
    void savingAndReloadingPreservesTasks() throws Exception {
        TaskService original = new TaskService();
        original.addTask("Write tests");
        original.addTask("Build runnable JAR");
        Path file = temporaryDirectory.resolve("tasks.ser");

        original.save(file);
        TaskService restored = new TaskService();
        restored.load(file);

        assertEquals(List.of(new Task("Write tests"), new Task("Build runnable JAR")),
                restored.getTasks());
    }
}
