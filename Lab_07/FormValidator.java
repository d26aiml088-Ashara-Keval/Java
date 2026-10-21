package Lab_07;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface NotBlank {}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface MaxLength {
    int value();
}

class SignupForm {
    @NotBlank
    String username;

    @NotBlank
    @MaxLength(20)
    String email;

    @MaxLength(10)
    String password;

    SignupForm(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}

public class FormValidator {
    static List<String> validate(Object obj)
            throws IllegalAccessException {
        List<String> errors = new ArrayList<>();

        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(obj);
            String text = value == null ? "" : value.toString();

            if (field.isAnnotationPresent(NotBlank.class)
                    && text.trim().isEmpty()) {
                errors.add(field.getName() + " must not be blank");
            }

            MaxLength max =
                    field.getAnnotation(MaxLength.class);

            if (max != null && text.length() > max.value()) {
                errors.add(field.getName()
                        + " exceeds maximum length " + max.value());
            }
        }

        return errors;
    }

    public static void main(String[] args)
            throws IllegalAccessException {
        SignupForm form =
                new SignupForm("", "verylongemailaddress@example.com",
                        "password123456");

        List<String> errors = validate(form);

        if (errors.isEmpty()) {
            System.out.println("Form is valid");
        } else {
            for (String error : errors) {
                System.out.println(error);
            }
        }
    }
}