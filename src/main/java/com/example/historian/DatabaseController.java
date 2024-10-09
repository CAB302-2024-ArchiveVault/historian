package com.example.historian;

import com.example.historian.auth.AuthSingleton;
import com.example.historian.models.account.Account;
import com.example.historian.models.account.AccountPrivilege;
import com.example.historian.utils.SqliteConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import com.example.historian.utils.StageManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class DatabaseController {
    private AuthSingleton authSingleton;
    private Connection connection;

    @FXML private Button exitButton;
    @FXML private Button auditLogButton;
    @FXML private Button accountManagementButton;
    @FXML private Button deleteDatabaseButton;

    @FXML
    public void initialize() throws IOException {
        connection = SqliteConnection.getInstance();

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
    protected void onDeleteDatabaseClick() throws IOException, SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Database Deletion Confirmation");
        alert.setHeaderText("Warning! You are about to delete the database. This will erase all data.");
        alert.setContentText("Are you sure you want to delete the database?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            deleteDatabase();
            StageManager.switchScene("homepage-view.fxml");
        } else {
            // IF THEY CANCEL DELETE
        }
    }

    private void deleteDatabase() throws SQLException {
        // Set autoCommit to false to manually manage transactions
        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            statement.execute("PRAGMA busy_timeout = 5000;");
            statement.execute("PRAGMA foreign_keys = OFF;");

            // Delete records first
            statement.execute("DELETE FROM photos;");
            statement.execute("DELETE FROM locations;");
            statement.execute("DELETE FROM people;");
            statement.execute("DELETE FROM tags;");

            // Commit the deletion operation
            connection.commit();

            statement.execute("PRAGMA foreign_keys = ON;");
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
