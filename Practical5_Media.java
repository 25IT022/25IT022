abstract class Media {
    String title;
    int lateDays;

    Media(String title, int lateDays) {
        this.title = title;
        this.lateDays = lateDays;
    }

    abstract double lateFee();
}

class Book extends Media {

    Book(String title, int lateDays) {
        super(title, lateDays);
    }

    double lateFee() {
        return lateDays * 2;
    }
}

class DVD extends Media {

    DVD(String title, int lateDays) {
        super(title, lateDays);
    }

    double lateFee() {
        return lateDays * 5;
    }
}

class Magazine extends Media {

    Magazine(String title, int lateDays) {
        super(title, lateDays);
    }

    double lateFee() {
        return lateDays * 3;
    }
}

public class Practical5_Media {

    public static void main(String[] args) {

        Media[] items = {
            new Book("Java Book", 3),
            new DVD("Avengers DVD", 2),
            new Magazine("Tech Magazine", 4)
        };

        double totalFee = 0;

        for (Media m : items) {

            double fee = m.lateFee();

            System.out.println(m.title + " Late Fee = " + fee);

            totalFee += fee;
        }

        System.out.println("Total Late Fee = " + totalFee);
    }
}