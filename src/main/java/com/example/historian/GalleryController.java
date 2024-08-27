package com.example.historian;

import com.example.historian.auth.AuthSingleton;
import com.example.historian.models.account.Account;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class GalleryController {
  @FXML
  private Button logoutButton;
  @FXML
  private Text accountText;

  private Account authorisedAccount;

  @FXML
  public void initialize() {
    // Get the Auth Singleton
    AuthSingleton authSingleton = AuthSingleton.getInstance();
    authorisedAccount = authSingleton.getAccount();

    // TODO: If the account does not exist, redirect to the homepage

    accountText.setText(authorisedAccount.getUsername());
  }

  @FXML
  protected void onlogoutButtonClick() throws IOException {
    Stage homepageStage = (Stage) logoutButton.getScene().getWindow();
    FXMLLoader fxmlLoader = new FXMLLoader(HistorianApplication.class.getResource("homepage-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), HistorianApplication.WIDTH, HistorianApplication.HEIGHT);
    homepageStage.setScene(scene);
  }
}
