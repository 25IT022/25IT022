package P14;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BankServiceTest {

    @Test
    public void testAddingAccountIncreasesCount() {

        BankService service = new BankService();

        service.addAccount("Rahul", 1000);

        assertEquals(1, service.getAccountCount());
    }

    @Test
    public void testAddingTwoAccounts() {

        BankService service = new BankService();

        service.addAccount("Rahul", 1000);
        service.addAccount("Priya", 2000);

        assertEquals(2, service.getAccountCount());
    }

    @Test
    public void testInvalidAccountThrowsException() {

        BankService service = new BankService();

        assertThrows(IllegalArgumentException.class, () -> {
            service.addAccount("", 1000);
        });
    }
}