package Lab_07;

import java.lang.annotation.*;
import java.lang.reflect.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Column {
    String name();
}

class Student {
    @Column(name = "student_name")
    String name;

    @Column(name = "age")
    int age;

    @Column(name = "city")
    String city;

    public String toString() {
        return "Name: " + name
                + ", Age: " + age
                + ", City: " + city;
    }
}

public class ColumnMapping {
    static void populate(Object obj, String[] header,
                         String[] data) throws Exception {
        Class<?> cls = obj.getClass();

        for (Field field : cls.getDeclaredFields()) {
            Column column = field.getAnnotation(Column.class);

            if (column == null) {
                continue;
            }

            int index = -1;

            for (int i = 0; i < header.length; i++) {
                if (header[i].trim().equals(column.name())) {
                    index = i;
                    break;
                }
            }

            if (index == -1 || index >= data.length) {
                System.out.println(
                        "Missing column: " + column.name());
                continue;
            }

            field.setAccessible(true);
            String value = data[index].trim();

            if (field.getType() == String.class) {
                field.set(obj, value);
            } else if (field.getType() == int.class) {
                field.setInt(obj, Integer.parseInt(value));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Student s = new Student();

        String[] header = {"student_name", "age"};
        String[] data = {"Keval", "18"};

        populate(s, header, data);

        System.out.println(s);
    }
}