package lab14.model;

import java.io.Serializable;
import java.util.Objects;

public final class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String title;

    public Task(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Task title must not be blank");
        }
        this.title = title.trim();
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Task task)) {
            return false;
        }
        return title.equals(task.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    @Override
    public String toString() {
        return title;
    }
}
