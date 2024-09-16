package com.example.historian;

import com.example.historian.auth.AuthSingleton;
import com.example.historian.models.account.Account;
import com.example.historian.models.account.AccountPrivilege;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import com.example.historian.utils.StageManager;

import java.io.IOException;
import java.util.Optional;

public class DatabaseController {
    private AuthSingleton authSingleton;

    @FXML
    private Button exitButton;
    @FXML
    private Button auditLogButton;
    @FXML
    private Button accountManagementButton;
    @FXML
    private Button deleteDatabaseButton;

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
    protected void onAuditLogClick() throws IOException {
        StageManager.switchScene("audit-log-view.fxml");
    }

    @FXML
    protected void onAccountManagementClick() throws IOException {
        StageManager.switchScene("account-management-view.fxml");
    }

    @FXML
    protected void onBackButtonClick() throws IOException {
        StageManager.switchScene("admin-options-view.fxml");
    }

    @FXML
    protected void onDeleteDatabaseClick() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Database Deletion Confirmation");
        alert.setHeaderText("Warning! You are about to delete the database. This will erase all data.");
        alert.setContentText("Are you sure you want to delete the database?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // IF THEY CONFIRM DELETE
            StageManager.switchScene("homepage-view.fxml");
        } else {
            // IF THEY CANCEL DELETE
            StageManager.switchScene("homepage-view.fxml");
        }
    }
}
