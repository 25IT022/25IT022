package P14;

import java.util.ArrayList;

public class BankService {

    private ArrayList<Account> accounts = new ArrayList<>();

    public void addAccount(String name, double balance) {

        if (name == null || name.trim().isEmpty() || balance < 0) {
            throw new IllegalArgumentException("Invalid account details");
        }

        accounts.add(new Account(name, balance));
    }

    public int getAccountCount() {
        return accounts.size();
    }

    public Account getAccount(int index) {

        if (index < 0 || index >= accounts.size()) {
            throw new IllegalArgumentException("Invalid account index");
        }

        return accounts.get(index);
    }
}