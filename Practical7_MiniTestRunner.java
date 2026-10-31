import java.lang.annotation.*;
import java.lang.reflect.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Run {
}

class MyTests {

    @Run
    public void testAddition() {
        System.out.println("testAddition ran");
    }

    @Run
    public void testSubtraction() {
        System.out.println("testSubtraction ran");
    }

    public void normalMethod() {
        System.out.println("Normal method ran");
    }

    @Run
    public void testMessage() {
        System.out.println("testMessage ran");
    }
}

public class Practical7_MiniTestRunner {

    public static void main(String[] args) {

        MyTests testObject = new MyTests();

        int count = 0;

        Method[] methods = MyTests.class.getDeclaredMethods();

        for (Method method : methods) {

            if (method.isAnnotationPresent(Run.class)
                    && method.getParameterCount() == 0) {

                try {
                    method.invoke(testObject);
                    count++;
                } catch (Exception e) {
                    System.out.println("Error running " + method.getName());
                }
            }
        }

        System.out.println("Total @Run methods executed: " + count);
    }
}