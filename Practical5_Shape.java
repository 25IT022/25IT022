abstract class Shape {
    abstract double area();
}

class Circle extends Shape {
    double radius;

    Circle(double radius) {
        this.radius = radius;
    }

    double area() {
        return 3.14 * radius * radius;
    }
}

class Rectangle extends Shape {
    double length, width;

    Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }

    double area() {
        return length * width;
    }
}

class Triangle extends Shape {
    double base, height;

    Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }

    double area() {
        return 0.5 * base * height;
    }
}

public class Practical5_Shape {

    public static void main(String[] args) {

        Shape[] shapes = {
            new Circle(5),
            new Rectangle(4, 6),
            new Triangle(8, 3)
        };

        double total = 0;
        double largest = 0;

        for (Shape s : shapes) {
            double a = s.area();

            System.out.println("Area = " + a);

            total += a;

            if (a > largest) {
                largest = a;
            }
        }

        System.out.println("Total Area = " + total);
        System.out.println("Largest Area = " + largest);
    }
}