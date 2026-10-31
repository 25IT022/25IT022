import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface NotBlank {
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface MaxLength {
    int value();
}

class SignupForm {

    @NotBlank
    @MaxLength(20)
    String name;

    @NotBlank
    @MaxLength(30)
    String email;

    @MaxLength(10)
    String phone;

    SignupForm(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}

public class Practical7_FormValidator {

    public static List<String> validate(Object obj) {
        List<String> errors = new ArrayList<>();

        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            try {
                Object value = field.get(obj);
                String text = value == null ? "" : value.toString();

                if (field.isAnnotationPresent(NotBlank.class)) {
                    if (text.trim().isEmpty()) {
                        errors.add(field.getName() + " must not be blank");
                    }
                }

                if (field.isAnnotationPresent(MaxLength.class)) {
                    int max = field.getAnnotation(MaxLength.class).value();

                    if (text.length() > max) {
                        errors.add(field.getName()
                                + " must have maximum "
                                + max + " characters");
                    }
                }

            } catch (IllegalAccessException e) {
                errors.add("Cannot access field: " + field.getName());
            }
        }

        return errors;
    }

    public static void main(String[] args) {

        SignupForm form = new SignupForm(
                "Priyanshi",
                "priyanshi@gmail.com",
                "9876543210"
        );

        List<String> errors = validate(form);

        if (errors.isEmpty()) {
            System.out.println("Form is valid.");
        } else {
            System.out.println("Validation Errors:");

            for (String error : errors) {
                System.out.println(error);
            }
        }
    }
}