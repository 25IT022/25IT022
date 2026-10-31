import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.lang.annotation.*;
import java.lang.reflect.Field;

/* CEUE203 OOP - Integrated MiniBank (Practicals 01-14)
   Requires JDK 17 or later. Save as MiniBank.java
*/

record BankInfo(String name, String branch) {}

enum MenuOption { CREATE, DEPOSIT, WITHDRAW, TRANSFER, DISPLAY, HISTORY, REPORT, EXIT }
enum TransactionType { DEPOSIT, WITHDRAW, TRANSFER }

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Id {}
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Positive {}
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface MaxLength { int value(); }

interface Transactable {
    void deposit(long amount) throws BankException;
    void withdraw(long amount) throws BankException;
}
interface InterestBearing {
    double calculateInterest();
    default void showInterest() { System.out.printf("Interest: Rs. %.2f%n", calculateInterest()); }
}
@FunctionalInterface
interface WithdrawRule extends Serializable {
    boolean canWithdraw(long balance, long amount);
}

class BankException extends Exception {
    BankException(String message) { super(message); }
}
class InvalidAmountException extends BankException {
    InvalidAmountException(String message) { super(message); }
}
class InsufficientFundsException extends BankException {
    InsufficientFundsException(String message) { super(message); }
}
class AccountNotFoundException extends BankException {
    AccountNotFoundException(String message) { super(message); }
}

class Customer implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;
    private static final AtomicInteger NEXT_ID = new AtomicInteger(101);
    @Id private final String customerId;
    @MaxLength(40) private String name;
    private String email;
    private String mobile;
    private Address address;

    static class Address implements Serializable {
        private static final long serialVersionUID = 1L;
        String city;
        String state;
        Address(String city, String state) { this.city = city; this.state = state; }
        public String toString() { return city + ", " + state; }
    }

    Customer(String name, String email, String mobile, Address address) {
        this.customerId = "CUST" + NEXT_ID.getAndIncrement();
        setName(name);
        setEmail(email);
        setMobile(mobile);
        this.address = address;
    }
    String getCustomerId() { return customerId; }
    String getName() { return name; }
    String getEmail() { return email; }
    String getMobile() { return mobile; }
    Address getAddress() { return address; }
    void setName(String name) {
        if (name == null || name.trim().isEmpty() || name.length() > 40) throw new IllegalArgumentException("Name must contain 1 to 40 characters.");
        this.name = name.trim();
    }
    void setEmail(String email) {
        if (!Validator.validEmail(email)) throw new IllegalArgumentException("Invalid email address.");
        this.email = email;
    }
    void setMobile(String mobile) {
        if (!Validator.validMobile(mobile)) throw new IllegalArgumentException("Mobile must contain 10 digits.");
        this.mobile = mobile;
    }
    @Override public Customer clone() {
        try {
            Customer copy = (Customer) super.clone();
            copy.address = address == null ? null : new Address(address.city, address.state);
            return copy;
        } catch (CloneNotSupportedException e) { throw new AssertionError(e); }
    }
    @Override public String toString() {
        return customerId + " | " + name + " | " + email + " | " + mobile + " | " + address;
    }
}

abstract class Account implements Transactable, Serializable {
    private static final long serialVersionUID = 1L;
    private static final AtomicInteger NEXT_ACCOUNT = new AtomicInteger(1);
    @Id private final String accountNumber;
    @Positive private long balance;
    private final String ownerName;
    private boolean active = true;
    private final WithdrawRule withdrawRule;

    Account(String ownerName, long openingBalance, WithdrawRule rule) throws InvalidAmountException {
        if (openingBalance < 0) throw new InvalidAmountException("Opening balance cannot be negative.");
        if (ownerName == null || ownerName.trim().isEmpty()) throw new IllegalArgumentException("Owner name is required.");
        this.accountNumber = String.format("AC%04d", NEXT_ACCOUNT.getAndIncrement());
        this.ownerName = ownerName.trim();
        this.balance = openingBalance;
        this.withdrawRule = rule;
    }
    public String getAccountNumber() { return accountNumber; }
    public String getOwnerName() { return ownerName; }
    public synchronized long getBalance() { return balance; }
    public boolean isActive() { return active; }
    public void close() { active = false; }
    @Override public synchronized void deposit(long amount) throws BankException {
        checkActive();
        if (amount <= 0) throw new InvalidAmountException("Deposit amount must be positive.");
        balance = Math.addExact(balance, amount);
    }
    @Override public synchronized void withdraw(long amount) throws BankException {
        checkActive();
        if (amount <= 0) throw new InvalidAmountException("Withdrawal amount must be positive.");
        if (!withdrawRule.canWithdraw(balance, amount)) throw new InsufficientFundsException("Insufficient balance or withdrawal limit exceeded.");
        balance -= amount;
    }
    private void checkActive() throws BankException {
        if (!active) throw new BankException("This account is inactive.");
    }
    abstract String getAccountType();
    @Override public String toString() {
        return accountNumber + " | " + ownerName + " | " + getAccountType() + " | Balance: Rs. " + getBalance() + " | " + (active ? "Active" : "Inactive");
    }
    @Override public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Account other)) return false;
        return accountNumber.equals(other.accountNumber);
    }
    @Override public int hashCode() { return accountNumber.hashCode(); }
}

class SavingsAccount extends Account implements InterestBearing {
    SavingsAccount(String owner, long balance) throws InvalidAmountException {
        super(owner, balance, (available, amount) -> amount <= available && available - amount >= 500);
    }
    String getAccountType() { return "Savings"; }
    public double calculateInterest() { return getBalance() * 0.04; }
}
class CurrentAccount extends Account {
    CurrentAccount(String owner, long balance) throws InvalidAmountException {
        super(owner, balance, (available, amount) -> amount <= available);
    }
    String getAccountType() { return "Current"; }
}
class FixedDepositAccount extends Account implements InterestBearing {
    FixedDepositAccount(String owner, long balance) throws InvalidAmountException {
        super(owner, balance, (available, amount) -> false);
    }
    String getAccountType() { return "Fixed Deposit"; }
    public double calculateInterest() { return getBalance() * 0.07; }
}

class Validator {
    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern MOBILE = Pattern.compile("^[0-9]{10}$");
    static boolean validEmail(String value) { return value != null && EMAIL.matcher(value).matches(); }
    static boolean validMobile(String value) { return value != null && MOBILE.matcher(value).matches(); }
    static boolean validAmount(long amount) { return amount > 0; }
}

record Command(TransactionType type, String accountNumber, long amount) {}
class CommandParser {
    static Command parse(String line) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 3) throw new IllegalArgumentException("Use: DEPOSIT/WITHDRAW accountNumber amount");
        TransactionType type = TransactionType.valueOf(parts[0].toUpperCase(Locale.ROOT));
        return new Command(type, parts[1], Long.parseLong(parts[2]));
    }
}

class GenericRepository<T> {
    private final Map<Object, T> data = new HashMap<>();
    private Object getId(T item) {
        try {
            for (Field field : item.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    return field.get(item);
                }
            }
            throw new IllegalStateException("No @Id field found in " + item.getClass().getSimpleName());
        } catch (ReflectiveOperationException e) { throw new IllegalStateException(e); }
    }
    void save(T item) { data.put(getId(item), item); }
    T findById(Object id) { return data.get(id); }
    List<T> findAll() { return new ArrayList<>(data.values()); }
    boolean delete(Object id) { return data.remove(id) != null; }
    int count() { return data.size(); }
}

class AnnotationValidator {
    static void validate(Object object) {
        try {
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(object);
                if (field.isAnnotationPresent(Positive.class) && value instanceof Number n && n.longValue() < 0)
                    throw new IllegalArgumentException(field.getName() + " must be positive.");
                MaxLength max = field.getAnnotation(MaxLength.class);
                if (max != null && value instanceof String s && s.length() > max.value())
                    throw new IllegalArgumentException(field.getName() + " exceeds maximum length.");
            }
        } catch (IllegalAccessException e) { throw new IllegalStateException(e); }
    }
}

record Transaction(String type, String from, String to, long amount, LocalDateTime time) implements Serializable {
    public String toString() {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " | " + type + " | " + from + (to == null ? "" : " -> " + to) + " | Rs. " + amount;
    }
}

class TransactionBuffer {
    private final Queue<Runnable> queue = new ArrayDeque<>();
    private boolean closed;
    synchronized void produce(Runnable task) throws InterruptedException {
        while (queue.size() >= 20 && !closed) wait();
        if (closed) throw new IllegalStateException("Transaction buffer is closed.");
        queue.add(task);
        notifyAll();
    }
    synchronized Runnable consume() throws InterruptedException {
        while (queue.isEmpty() && !closed) wait();
        if (queue.isEmpty()) return null;
        Runnable task = queue.remove();
        notifyAll();
        return task;
    }
    synchronized void close() { closed = true; notifyAll(); }
}

class TransactionProcessor implements AutoCloseable {
    private final ExecutorService pool = Executors.newFixedThreadPool(4);
    void submit(Runnable task) { pool.submit(task); }
    public void close() {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) pool.shutdownNow();
        } catch (InterruptedException e) { pool.shutdownNow(); Thread.currentThread().interrupt(); }
    }
}

class BankService {
    private final Map<String, Account> accounts = new HashMap<>();
    private final List<Transaction> history = new ArrayList<>();
    private final ConcurrentHashMap<String, String> liveStatus = new ConcurrentHashMap<>();
    private final GenericRepository<Account> repository = new GenericRepository<>();
    private final TransactionProcessor processor = new TransactionProcessor();

    synchronized void addAccount(Account account) {
        AnnotationValidator.validate(account);
        accounts.put(account.getAccountNumber(), account);
        repository.save(account);
        liveStatus.put(account.getAccountNumber(), "ACTIVE");
    }
    synchronized Account findAccount(String number) throws AccountNotFoundException {
        Account account = accounts.get(number);
        if (account == null) throw new AccountNotFoundException("Account not found: " + number);
        return account;
    }
    void deposit(String number, long amount) throws BankException {
        Account account = findAccount(number);
        account.deposit(amount);
        addHistory(new Transaction("DEPOSIT", number, null, amount, LocalDateTime.now()));
    }
    void withdraw(String number, long amount) throws BankException {
        Account account = findAccount(number);
        account.withdraw(amount);
        addHistory(new Transaction("WITHDRAW", number, null, amount, LocalDateTime.now()));
    }
    void transfer(String from, String to, long amount) throws BankException {
        if (from.equals(to)) throw new BankException("Source and destination accounts must be different.");
        Account source = findAccount(from);
        Account destination = findAccount(to);
        Account first = from.compareTo(to) < 0 ? source : destination;
        Account second = first == source ? destination : source;
        synchronized (first) {
            synchronized (second) {
                source.withdraw(amount);
                try { destination.deposit(amount); }
                catch (BankException e) { source.deposit(amount); throw e; }
            }
        }
        addHistory(new Transaction("TRANSFER", from, to, amount, LocalDateTime.now()));
    }
    synchronized void addHistory(Transaction transaction) { history.add(transaction); }
    synchronized List<Transaction> getHistory() { return new ArrayList<>(history); }
    synchronized List<Account> getAccounts() {
        List<Account> result = new ArrayList<>(accounts.values());
        result.sort(Comparator.comparing(Account::getAccountNumber));
        return result;
    }
    int repositoryCount() { return repository.count(); }
    ConcurrentHashMap<String, String> getLiveStatus() { return liveStatus; }
    void submitDeposit(String number, long amount) {
        processor.submit(() -> {
            try { deposit(number, amount); System.out.println("Background deposit completed for " + number); }
            catch (BankException e) { System.out.println("Background transaction failed: " + e.getMessage()); }
        });
    }
    void close() { processor.close(); }
    void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(Path.of("minibank-data.ser")))) {
            out.writeObject(new ArrayList<>(accounts.values()));
            out.writeObject(new ArrayList<>(history));
            System.out.println("Data saved to minibank-data.ser");
        } catch (IOException e) { System.out.println("Could not save data: " + e.getMessage()); }
    }
    void generateReport() {
        try {
            StringBuilder report = new StringBuilder("MINIBANK END-OF-DAY REPORT\n");
            report.append("Generated: ").append(LocalDateTime.now()).append("\n\nACCOUNTS\n");
            for (Account account : getAccounts()) report.append(account).append('\n');
            report.append("\nTRANSACTIONS\n");
            for (Transaction transaction : getHistory()) report.append(transaction).append('\n');
            Files.writeString(Path.of("minibank-report.txt"), report.toString());
            System.out.println("Report generated: minibank-report.txt");
        } catch (IOException e) { System.out.println("Could not generate report: " + e.getMessage()); }
    }
    void printStatement(String number) throws AccountNotFoundException {
        findAccount(number);
        StringBuilder statement = new StringBuilder("\n--- ACCOUNT STATEMENT ---\nAccount: ").append(number).append('\n');
        for (Transaction t : getHistory()) if (t.from().equals(number) || number.equals(t.to())) statement.append(t).append('\n');
        System.out.println(statement);
    }
}

public class MiniBank {
    private static final Scanner sc = new Scanner(System.in);
    private static final BankService bank = new BankService();
    private static final GenericRepository<Customer> customers = new GenericRepository<>();
    private static final BankInfo bankInfo = new BankInfo("MiniBank", "CHARUSAT Branch");

    private static long readAmount() {
        System.out.print("Enter amount: ");
        try { return Long.parseLong(sc.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("Please enter a valid whole number."); return -1; }
    }
    private static void createAccount() {
        try {
            System.out.print("Customer name: "); String name = sc.nextLine().trim();
            System.out.print("Email: "); String email = sc.nextLine().trim();
            System.out.print("10-digit mobile: "); String mobile = sc.nextLine().trim();
            System.out.print("City: "); String city = sc.nextLine().trim();
            System.out.print("State: "); String state = sc.nextLine().trim();
            Customer customer = new Customer(name, email, mobile, new Customer.Address(city, state));
            customers.save(customer);
            System.out.println("Account type: 1. Savings  2. Current  3. Fixed Deposit");
            System.out.print("Choose type: "); int type = Integer.parseInt(sc.nextLine().trim());
            long opening = readAmount();
            if (opening < 0) throw new InvalidAmountException("Opening balance cannot be negative.");
            Account account;
            switch (type) {
                case 1 -> account = new SavingsAccount(name, opening);
                case 2 -> account = new CurrentAccount(name, opening);
                case 3 -> account = new FixedDepositAccount(name, opening);
                default -> throw new IllegalArgumentException("Invalid account type.");
            }
            bank.addAccount(account);
            System.out.println("\nAccount created successfully!\n" + customer + "\n" + account);
        } catch (Exception e) { System.out.println("Unable to create account: " + e.getMessage()); }
    }
    private static void showAccounts() {
        System.out.println("\n--- ALL ACCOUNTS ---");
        List<Account> list = bank.getAccounts();
        if (list.isEmpty()) System.out.println("No accounts available.");
        else list.forEach(System.out::println);
        System.out.println("Repository records: " + bank.repositoryCount());
    }
    private static void processDeposit() {
        try { System.out.print("Account number: "); String no = sc.nextLine().trim(); long amount = readAmount(); bank.deposit(no, amount); System.out.println("Deposit successful."); }
        catch (BankException e) { System.out.println("Transaction failed: " + e.getMessage()); }
    }
    private static void processWithdraw() {
        try { System.out.print("Account number: "); String no = sc.nextLine().trim(); long amount = readAmount(); bank.withdraw(no, amount); System.out.println("Withdrawal successful."); }
        catch (BankException e) { System.out.println("Transaction failed: " + e.getMessage()); }
    }
    private static void processTransfer() {
        try {
            System.out.print("From account: "); String from = sc.nextLine().trim();
            System.out.print("To account: "); String to = sc.nextLine().trim();
            long amount = readAmount(); bank.transfer(from, to, amount);
            System.out.println("Transfer successful.");
        } catch (BankException e) { System.out.println("Transfer failed: " + e.getMessage()); }
    }
    private static void showHistory() {
        List<Transaction> list = bank.getHistory();
        System.out.println("\n--- TRANSACTION HISTORY ---");
        if (list.isEmpty()) System.out.println("No transactions yet.");
        else list.forEach(System.out::println);
    }
    private static MenuOption getOption(String input) {
        try {
            int choice = Integer.parseInt(input);
            return switch (choice) {
                case 1 -> MenuOption.CREATE;
                case 2 -> MenuOption.DEPOSIT;
                case 3 -> MenuOption.WITHDRAW;
                case 4 -> MenuOption.TRANSFER;
                case 5 -> MenuOption.DISPLAY;
                case 6 -> MenuOption.HISTORY;
                case 7 -> MenuOption.REPORT;
                case 0 -> MenuOption.EXIT;
                default -> null;
            };
        } catch (NumberFormatException e) { return null; }
    }
    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println("       " + bankInfo.name().toUpperCase() + " MANAGEMENT SYSTEM");
        System.out.println("       " + bankInfo.branch());
        System.out.println("====================================");
        try {
            boolean running = true;
            while (running) {
                System.out.println("\n1. Create Account\n2. Deposit\n3. Withdraw\n4. Transfer\n5. Display Accounts\n6. Transaction History\n7. Generate Report\n0. Save and Exit");
                System.out.print("Enter choice: ");
                MenuOption option = getOption(sc.nextLine().trim());
                try {
                    if (option == null) { System.out.println("Invalid choice. Try again."); continue; }
                    switch (option) {
                        case CREATE -> createAccount();
                        case DEPOSIT -> processDeposit();
                        case WITHDRAW -> processWithdraw();
                        case TRANSFER -> processTransfer();
                        case DISPLAY -> showAccounts();
                        case HISTORY -> showHistory();
                        case REPORT -> bank.generateReport();
                        case EXIT -> { bank.saveData(); running = false; }
                    }
                } catch (RuntimeException e) { System.out.println("Operation error: " + e.getMessage()); }
            }
        } finally {
            bank.close();
            sc.close();
            System.out.println("Thank you for using MiniBank.");
        }
    }
}
