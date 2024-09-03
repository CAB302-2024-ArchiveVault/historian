package com.example.historian;

import com.example.historian.auth.AuthSingleton;
import com.example.historian.models.account.Account;
import com.example.historian.models.account.AccountDAOSingleton;
import com.example.historian.models.account.IAccountDAO;
import com.example.historian.utils.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class HomepageController {

  @FXML
  private Button loginButton;
  @FXML
  private Button databaseButton;

  @FXML
  private TextField usernameField;
  @FXML
  private PasswordField passwordField;
  @FXML
  private Text errorText;

  //When the database button is clicked, creates and swaps to the database stage + scene
  @FXML
  protected void onDatabaseButtonClick() throws IOException {
    StageManager.switchScene("database-view.fxml");
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
    IAccountDAO accountDAO = AccountDAOSingleton.getInstance().getAccountDAO();
    Account account = accountDAO.getAccount(inputtedUsername);
    if (account == null) {
      showError("This account does not exist.");
      return;
    }

    // Check if the password is correct
    boolean isPasswordCorrect = account.comparePassword(inputtedPassword);
    if (!isPasswordCorrect) {
      showError("Password is incorrect.");
      return;
    }

    // Set the validated account in the Auth Singleton
    AuthSingleton authSingleton = AuthSingleton.getInstance();
    authSingleton.setAccount(account);

    // Move to the gallery stage
    StageManager.switchScene("gallery-view.fxml");
  }

  private void showError(String message) {
    errorText.setText(message);
    errorText.setVisible(true);
  }

  private void hideError() {
    errorText.setVisible(false);
  }
}
