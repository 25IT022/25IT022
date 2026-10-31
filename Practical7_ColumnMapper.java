import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Column {
    String name();
}

class Student {

    @Column(name = "id")
    int id;

    @Column(name = "name")
    String name;

    @Column(name = "email")
    String email;

    public void display() {
        System.out.println("ID    : " + id);
        System.out.println("Name  : " + name);
        System.out.println("Email : " + email);
    }
}

public class Practical7_ColumnMapper {

    public static void populate(
            Object obj,
            String[] header,
            String[] data) throws Exception {

        Map<String, String> values = new HashMap<>();

        for (int i = 0; i < header.length && i < data.length; i++) {
            values.put(header[i], data[i]);
        }

        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {

            if (field.isAnnotationPresent(Column.class)) {

                Column column = field.getAnnotation(Column.class);
                String columnName = column.name();

                if (!values.containsKey(columnName)) {
                    System.out.println(
                            "Missing column: " + columnName);
                    continue;
                }

                String value = values.get(columnName);

                field.setAccessible(true);

                if (field.getType() == int.class) {
                    field.setInt(obj, Integer.parseInt(value));
                } else if (field.getType() == String.class) {
                    field.set(obj, value);
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {

        String[] header = {"id", "name", "email"};
        String[] data = {"101", "Priyanshi", "priya@example.com"};

        Student student = new Student();

        populate(student, header, data);

        System.out.println("Student Details:");
        student.display();
    }
}