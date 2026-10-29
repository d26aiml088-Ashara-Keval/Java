package util;

import model.annotation.Audit;

import java.lang.reflect.Method;
import java.util.logging.Logger;

public final class AuditLogger {
    private static final Logger LOGGER = Logger.getLogger(AuditLogger.class.getName());

    private AuditLogger() {
    }

    public static void record(Object target, String methodName, Class<?>... parameterTypes) {
        if (target == null || methodName == null) {
            throw new IllegalArgumentException("Target and method name are required");
        }
        try {
            Method method = target.getClass().getMethod(methodName, parameterTypes);
            if (method.isAnnotationPresent(Audit.class)) {
                LOGGER.info(() -> "Service method invoked: "
                        + target.getClass().getSimpleName() + "." + methodName);
            }
        } catch (NoSuchMethodException exception) {
            throw new IllegalStateException("Audited service method was not found", exception);
        }
    }
}
