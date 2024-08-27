package com.example.historian;

import com.example.historian.models.Account;
import com.example.historian.models.AccountDAOSingleton;
import com.example.historian.models.MockAccountDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
    Stage databaseStage = (Stage) databaseButton.getScene().getWindow();
    FXMLLoader fxmlLoader = new FXMLLoader(HistorianApplication.class.getResource("database-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), HistorianApplication.WIDTH, HistorianApplication.HEIGHT);
    databaseStage.setScene(scene);
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

    // Check if the account exists and the password is correct
    MockAccountDAO accountDAO = AccountDAOSingleton.getInstance().getAccountDAO();
    Account account = accountDAO.getAccount(inputtedUsername);
    if (account == null) {
      showError("This account does not exist.");
      return;
    }

    boolean isPasswordCorrect = account.comparePassword(inputtedPassword);
    if (!isPasswordCorrect) {
      showError("Password is incorrect.");
    } else {
      Stage galleryStage = (Stage) loginButton.getScene().getWindow();
      FXMLLoader fxmlLoader = new FXMLLoader(HistorianApplication.class.getResource("gallery-view.fxml"));
      Scene scene = new Scene(fxmlLoader.load(), HistorianApplication.WIDTH, HistorianApplication.HEIGHT);
      galleryStage.setScene(scene);
    }
  }

  private void showError(String message) {
    errorText.setText(message);
    errorText.setVisible(true);
  }
  private void hideError() {
    errorText.setVisible(false);
  }
}
