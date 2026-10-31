class Stack<T> {

    private T[] items;
    private int top;

    @SuppressWarnings("unchecked")
    public Stack(int size) {
        items = (T[]) new Object[size];
        top = -1;
    }

    public void push(T value) {

        if (top == items.length - 1) {
            System.out.println("Stack is full.");
            return;
        }

        items[++top] = value;
        System.out.println("Pushed: " + value);
    }

    public T pop() {

        if (top == -1) {
            System.out.println("Stack is empty.");
            return null;
        }

        T value = items[top];
        items[top] = null;
        top--;

        return value;
    }

    public boolean isEmpty() {
        return top == -1;
    }
}

public class Practical13_GenericStack {

    public static void main(String[] args) {

        System.out.println("Integer Stack:");

        Stack<Integer> integerStack = new Stack<>(5);

        integerStack.push(10);
        integerStack.push(20);
        integerStack.push(30);

        System.out.println("Popped: "
                + integerStack.pop());

        System.out.println("Popped: "
                + integerStack.pop());

        System.out.println("Popped: "
                + integerStack.pop());

        System.out.println("Popped: "
                + integerStack.pop());

        System.out.println();

        System.out.println("String Stack:");

        Stack<String> stringStack = new Stack<>(5);

        stringStack.push("Java");
        stringStack.push("Python");
        stringStack.push("C++");

        System.out.println("Popped: "
                + stringStack.pop());

        System.out.println("Popped: "
                + stringStack.pop());

        System.out.println("Popped: "
                + stringStack.pop());
    }
}