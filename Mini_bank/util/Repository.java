package util;

import model.annotation.Id;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class Repository<T> {
    private final HashMap<String, T> entities = new HashMap<>();

    public synchronized T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }
        entities.put(extractId(entity), entity);
        return entity;
    }

    @SafeVarargs
    public final ArrayList<T> saveAll(T... items) {
        if (items == null) {
            throw new IllegalArgumentException("Items must not be null");
        }
        ArrayList<T> saved = new ArrayList<>(items.length);
        for (T item : items) {
            saved.add(save(item));
        }
        return saved;
    }

    public synchronized T findById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        return entities.get(id);
    }

    public synchronized ArrayList<T> findAll() {
        return new ArrayList<>(entities.values());
    }

    public synchronized boolean delete(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        return entities.remove(id) != null;
    }

    private String extractId(T entity) {
        Field idField = null;
        for (Class<?> type = entity.getClass(); type != null && type != Object.class;
                type = type.getSuperclass()) {
            for (Field field : type.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    if (idField != null) {
                        throw new IllegalArgumentException("Entity has multiple @Id fields");
                    }
                    idField = field;
                }
            }
        }
        if (idField == null || idField.getType() != String.class) {
            throw new IllegalArgumentException(
                    "Entity must have exactly one @Id field of type String");
        }

        try {
            if (!idField.canAccess(entity)) {
                idField.setAccessible(true);
            }
            Object value = idField.get(entity);
            if (!(value instanceof String id) || id.isBlank()) {
                throw new IllegalArgumentException("@Id value must not be blank");
            }
            return id;
        } catch (IllegalAccessException | SecurityException exception) {
            throw new IllegalArgumentException("Unable to read entity @Id field", exception);
        }
    }
}
