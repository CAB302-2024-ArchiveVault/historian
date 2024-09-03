package com.example.historian;

import com.example.historian.auth.AuthSingleton;
import com.example.historian.models.account.Account;
import com.example.historian.utils.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.IOException;

public class GalleryController {
  @FXML
  private Button logoutButton;
  @FXML
  private Text accountText;

  private AuthSingleton authSingleton;

  @FXML
  public void initialize() throws IOException {
    // Get the Auth Singleton
    authSingleton = AuthSingleton.getInstance();
    if (!authSingleton.checkAuthorised()) {
      StageManager.switchToHomepage();
    }

    Account authorisedAccount = authSingleton.getAccount();
    accountText.setText(authorisedAccount.getUsername());
  }

  @FXML
  protected void onLogoutButtonClick() throws IOException {
    authSingleton.signOut();
    StageManager.switchToHomepage();
  }
}
