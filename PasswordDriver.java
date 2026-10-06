class PasswordChecker {

    String s;
    int rules = 0;

    PasswordChecker() {
        s = null;
    }

    PasswordChecker(String c) {
        s = c;
    }

    void length() {
        if (s.length() >= 8)
            rules++;
    }

    void uppercase() {
        boolean flag = false;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) >= 'A' && s.charAt(i) <= 'Z')
                flag = true;
        }

        if (flag == true)
            rules++;
    }

    void digit() {
        boolean flag = false;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) >= '0' && s.charAt(i) <= '9')
                flag = true;
        }

        if (flag == true)
            rules++;
    }

    void specialchar() {
        boolean flag = false;

        for (int i = 0; i < s.length(); i++) {
            if (!Character.isLetterOrDigit(s.charAt(i)))
                flag = true;
        }

        if (flag == true)
            rules++;
    }

    void strength() {
        if (rules >= 0 && rules <= 1)
            System.out.println(s + " weak");

        if (rules >= 2 && rules <= 3)
            System.out.println(s + " medium");

        if (rules >= 4)
            System.out.println(s + " strong");
    }
}

public class PasswordDriver {

    public static void main(String[] args) {

        PasswordChecker p1 = new PasswordChecker("abc@123");

        p1.digit();
        p1.length();
        p1.specialchar();
        p1.uppercase();
        p1.strength();
    }
}