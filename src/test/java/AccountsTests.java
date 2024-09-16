import com.example.historian.models.account.Account;
import com.example.historian.models.account.AccountPrivilege;
import com.example.historian.models.account.SqliteAccountDAO;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SqliteAccountDAOTests {

    private static SqliteAccountDAO dao;

    @BeforeAll
    public static void setup() {
        // Initialize DAO (add sample data to in memory database)
        dao = new SqliteAccountDAO();
    }

    @Test
    public void testAddAccount() {
        Account newAccount = new Account("new_user", "new_password", AccountPrivilege.MEMBER);
        dao.addAccount(newAccount);

        Account retrievedAccount = dao.getAccount(newAccount.getUsername());
        assertNotNull(retrievedAccount);
        assertEquals("new_user", retrievedAccount.getUsername());
        assertEquals(AccountPrivilege.MEMBER, retrievedAccount.getAccountPrivilege());
    }

    @Test
    public void testGetAccountByUsername() {
        Account account = dao.getAccount("i_am_admin");
        assertNotNull(account);
        assertEquals("i_am_admin", account.getUsername());
        assertEquals(AccountPrivilege.ADMIN, account.getAccountPrivilege());
    }

    @Test
    public void testUpdateAccount() {
        Account account = dao.getAccount("i_am_curator");
        assertNotNull(account);

        account.setAccountPrivilege(AccountPrivilege.ADMIN);
        dao.updateAccount(account);

        Account updatedAccount = dao.getAccount("i_am_curator");
        assertEquals(AccountPrivilege.ADMIN, updatedAccount.getAccountPrivilege());
    }

    @Test
    public void testRemoveAccount() {
        Account account = new Account("delete_user", "password", AccountPrivilege.MEMBER);
        dao.addAccount(account);

        // Check to see account was added
        Account addedAccount = dao.getAccount("delete_user");
        assertNotNull(addedAccount);

        // Remove the account
        dao.removeAccount(addedAccount);

        // Check to see the account no longer exists
        Account deletedAccount = dao.getAccount("delete_user");
        assertNull(deletedAccount);
    }

    @Test
    public void testGetAllAccounts() {
        List<Account> accounts = dao.getAllAccounts();
        assertNotNull(accounts);
        assertFalse(accounts.isEmpty(), "The account list should not be empty");
    }
}


