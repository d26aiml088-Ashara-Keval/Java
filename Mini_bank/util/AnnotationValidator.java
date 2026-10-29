package util;

import model.annotation.MaxLength;
import model.annotation.Positive;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class AnnotationValidator {
    private AnnotationValidator() {
    }

    public static List<String> validate(Object entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }
        List<String> errors = new ArrayList<>();
        for (Class<?> type = entity.getClass(); type != null && type != Object.class;
                type = type.getSuperclass()) {
            for (Field field : type.getDeclaredFields()) {
                if (!field.isAnnotationPresent(MaxLength.class)
                        && !field.isAnnotationPresent(Positive.class)) {
                    continue;
                }
                try {
                    if (!field.canAccess(entity)) {
                        field.setAccessible(true);
                    }
                    Object value = field.get(entity);
                    MaxLength maxLength = field.getAnnotation(MaxLength.class);
                    if (maxLength != null && value instanceof String text
                            && text.length() > maxLength.value()) {
                        errors.add(field.getName() + " must contain at most "
                                + maxLength.value() + " characters");
                    }
                    if (field.isAnnotationPresent(Positive.class)
                            && (!(value instanceof Number number) || number.longValue() <= 0)) {
                        errors.add(field.getName() + " must be positive");
                    }
                } catch (IllegalAccessException | SecurityException exception) {
                    throw new IllegalStateException(
                            "Unable to validate field " + field.getName(), exception);
                }
            }
        }
        return List.copyOf(errors);
    }
}
