import com.example.historian.models.account.Account;
import com.example.historian.models.account.AccountPrivilege;
import com.example.historian.models.account.Password;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountTest {
  private Account account;

  @BeforeEach
  public void setup() {
    account = new Account("my_username", "my_password", AccountPrivilege.ADMIN);
  }

  @Test
  public void testGetId() {
    account.setId(1);
    assertEquals(1, account.getId());
  }

  @Test
  public void testGetUsername() {
    assertEquals("my_username", account.getUsername());
  }

  @Test
  public void testGetPassword() throws Exception {
    assertEquals(true, account.getPassword().compare("my_password"));
  }

  @Test
  public void testResetPassword() throws Exception {
    account.resetPassword("my_new_password");
    assertEquals(true, account.getPassword().compare("my_new_password"));
  }

  @Test
  public void testGetAccountPrivilege() {
    assertEquals(AccountPrivilege.ADMIN, account.getAccountPrivilege());
  }

  @Test
  public void testSetAccountPrivilege() {
    account.setAccountPrivilege(AccountPrivilege.CURATOR);
    assertEquals(AccountPrivilege.CURATOR, account.getAccountPrivilege());
  }
}
