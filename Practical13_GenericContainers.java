class Box<T> {

    private T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}

class Pair<A, B> {

    private A first;
    private B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }
}

public class Practical13_GenericContainers {

    public static <T> T firstElement(T[] array) {
        return array[0];
    }

    public static void main(String[] args) {

        Box<Integer> integerBox = new Box<>();
        integerBox.set(100);

        Box<String> stringBox = new Box<>();
        stringBox.set("Hello Java");

        System.out.println("Integer Box: " + integerBox.get());
        System.out.println("String Box: " + stringBox.get());

        Pair<String, Integer> student =
                new Pair<>("Priyanshi", 101);

        System.out.println("Pair First: " + student.getFirst());
        System.out.println("Pair Second: " + student.getSecond());

        Integer[] numbers = {10, 20, 30};
        String[] names = {"Java", "C++", "Python"};

        System.out.println(
                "First number: " + firstElement(numbers));

        System.out.println(
                "First name: " + firstElement(names));
    }
}