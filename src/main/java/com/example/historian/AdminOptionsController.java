package com.example.historian;

import com.example.historian.auth.AuthSingleton;
import com.example.historian.models.account.Account;
import com.example.historian.models.account.AccountPrivilege;
import com.example.historian.utils.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class AdminOptionsController {
    private AuthSingleton authSingleton;

    @FXML
    private Button databaseManagementButton;
    @FXML
    private Button galleriesButton;
    @FXML
    private Button logoutButton;

    @FXML
    public void initialize() throws IOException {
        authSingleton = AuthSingleton.getInstance();
        if (!authSingleton.checkAuthorised()) {
            StageManager.switchToHomepage();
        }

        Account curAccount = authSingleton.getAccount();
        if (curAccount == null || curAccount.getAccountPrivilege() != AccountPrivilege.DATABASE_OWNER) {
            StageManager.switchToHomepage();
        }
    }

    @FXML
    public void onDatabaseManagementClick() throws IOException {
        StageManager.switchScene("database-view.fxml");
    }

    @FXML
    public void onGalleriesClick() throws IOException {
        StageManager.switchScene("gallery-view.fxml");
    }

    @FXML
    public void onLogoutClick() throws IOException {
        authSingleton.signOut();
        StageManager.switchToHomepage();
    }
}
