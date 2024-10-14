package com.example.historian;

import com.example.historian.auth.AuthSingleton;
import com.example.historian.models.account.Account;
import com.example.historian.models.account.AccountPrivilege;
import com.example.historian.models.account.IAccountDAO;
import com.example.historian.models.account.SqliteAccountDAO;
import com.example.historian.models.gallery.Gallery;
import com.example.historian.models.gallery.IGalleryDAO;
import com.example.historian.models.gallery.SqliteGalleryDAO;
import com.example.historian.utils.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class HomepageController {
  private Boolean codeMode;

  @FXML private Button loginButton;
  @FXML private TextField usernameField;
  @FXML private PasswordField passwordField;
  @FXML private Text errorText;
  @FXML
  private GridPane loginGridPane;
  @FXML
  private GridPane codeGridPane;
  @FXML
  private Button useCodeButton;
  @FXML
  private Button codeButton;
  @FXML
  private TextField codeField;

  @FXML
  public void initialize() throws IOException {
    codeMode = false;
  }

  @FXML
  protected void onLoginButtonClick() throws IOException {
    hideError();

    // Get the value of the username and password fields
    String inputtedUsername = usernameField.getText();
    String inputtedPassword = passwordField.getText();
    if (inputtedUsername.isBlank() || inputtedUsername.isEmpty() || inputtedPassword.isBlank() || inputtedPassword.isEmpty()) {
      showError("Username and/or password fields are blank.");
      return;
    }

    // Check if the account exists
    IAccountDAO accountDAO = new SqliteAccountDAO();
    Account account = accountDAO.getAccount(inputtedUsername);
    if (account == null) {
      showError("This account does not exist.");
      return;
    }

    // Check if the password is correct
    try {
      boolean isPasswordCorrect = account.getPassword().compare(inputtedPassword);
      if (!isPasswordCorrect) {
        showError("Password is incorrect.");
        return;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Set the validated account in the Auth Singleton
    AuthSingleton authSingleton = AuthSingleton.getInstance();
    authSingleton.setAccount(account);

    Account authorisedAccount = authSingleton.getAccount();
    if (authorisedAccount.getAccountPrivilege() == AccountPrivilege.DATABASE_OWNER) {
      StageManager.switchScene("admin-options-view.fxml");
    } else {
      StageManager.switchScene("gallery-view.fxml",1000,900);
    }
  }

  private void showError(String message) {
    errorText.setText(message);
    errorText.setVisible(true);
  }

  private void hideError() {
    errorText.setVisible(false);
  }

  private void showUseCode() {
    loginGridPane.setVisible(!codeMode);
    loginGridPane.setManaged(!codeMode);
    codeGridPane.setVisible(codeMode);
    codeGridPane.setManaged(codeMode);
    hideError();
  }

  @FXML
  public void onUseCodeButtonClick() throws IOException {
    codeMode = !codeMode;
    if (codeMode) {
      useCodeButton.setText("Use an account");
    } else {
      useCodeButton.setText("Use a code");
    }
    showUseCode();
  }

  @FXML
  public void onCodeButtonClick() throws IOException {
    hideError();

    String inputtedCode = codeField.getText();
    if (inputtedCode.isBlank() || inputtedCode.isEmpty()) {
      showError("Code field is blank.");
      return;
    }
    IGalleryDAO galleryDAO = new SqliteGalleryDAO();
    Boolean galleryExists = galleryDAO.checkIfGalleryExistsByKey(inputtedCode);
    if (!galleryExists) {
      showError("This gallery does not exist.");
      return;
    }

    GalleryController.galleryCode = inputtedCode;
    StageManager.switchScene("gallery-view.fxml");
  }
}
