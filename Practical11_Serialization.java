import java.io.*;

class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    int id;
    String name;

    // This field will NOT be saved
    transient String password;

    Student(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    void display() {
        System.out.println("ID       : " + id);
        System.out.println("Name     : " + name);
        System.out.println("Password : " + password);
        System.out.println();
    }
}

public class SaveLoad {

    public static void main(String[] args) {

        String fileName = "students.dat";

        Student[] students = {
            new Student(101, "Priyanshi", "pass123"),
            new Student(102, "Rutvi", "pass456"),
            new Student(103, "Riya", "pass789")
        };

        try (ObjectOutputStream out =
                new ObjectOutputStream(new FileOutputStream(fileName))) {

            out.writeObject(students);

            System.out.println("Students saved successfully.");

        } catch (IOException e) {
            System.out.println("Error while saving: " + e.getMessage());
        }

        try (ObjectInputStream in =
                new ObjectInputStream(new FileInputStream(fileName))) {

            Student[] loadedStudents = (Student[]) in.readObject();

            System.out.println("\nLoaded Students:");

            for (Student student : loadedStudents) {
                student.display();
            }

            System.out.println(
                "Notice: transient password values became null."
            );

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error while loading: " + e.getMessage());
        }
    }
}