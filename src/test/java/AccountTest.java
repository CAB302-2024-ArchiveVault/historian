import com.example.historian.models.account.Account;
import com.example.historian.models.account.AccountPrivilege;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountTest {
  private Account account;

  @BeforeEach
  protected void setup() {
    account = new Account("my_username", "my_password", AccountPrivilege.ADMIN);
  }

  @Test
  protected void testGetId() {
    account.setId(1);
    assertEquals(1, account.getId());
  }

  @Test
  protected void testGetUsername() {
    assertEquals("my_username", account.getUsername());
  }

  @Test
  protected void testGetPassword() throws Exception {
      assertTrue(account.getPassword().compare("my_password"));
  }

  @Test
  protected void testResetPassword() throws Exception {
    account.resetPassword("my_new_password");
      assertTrue(account.getPassword().compare("my_new_password"));
  }

  @Test
  protected void testGetAccountPrivilege() {
    assertEquals(AccountPrivilege.ADMIN, account.getAccountPrivilege());
  }

  @Test
  protected void testSetAccountPrivilege() {
    account.setAccountPrivilege(AccountPrivilege.CURATOR);
    assertEquals(AccountPrivilege.CURATOR, account.getAccountPrivilege());
  }
}
