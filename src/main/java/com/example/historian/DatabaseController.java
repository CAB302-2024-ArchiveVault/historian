package com.example.historian;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class DatabaseController {
    @FXML
    private Button exitButton;
    @FXML
    private Button auditLogButton;
    @FXML
    private Button accountManagementButton;
    @FXML
    private Button deleteDatabaseButton;

    @FXML
    protected void onAuditLogClick() throws IOException {
        Stage homepageStage = (Stage) auditLogButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HistorianApplication.class.getResource("audit-log-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HistorianApplication.WIDTH, HistorianApplication.HEIGHT);
        homepageStage.setScene(scene);
    }

    @FXML
    protected void onAccountManagementClick() throws IOException {
        Stage homepageStage = (Stage) accountManagementButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HistorianApplication.class.getResource("account-management-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HistorianApplication.WIDTH, HistorianApplication.HEIGHT);
        homepageStage.setScene(scene);
    }

    @FXML
    protected void onExitButtonClick() throws IOException {
        Stage homepageStage = (Stage) exitButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HistorianApplication.class.getResource("homepage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HistorianApplication.WIDTH, HistorianApplication.HEIGHT);
        homepageStage.setScene(scene);
    }

    @FXML
    protected void onDeleteDatabaseClick() throws IOException {
        Stage homepageStage = (Stage) deleteDatabaseButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HistorianApplication.class.getResource("homepage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HistorianApplication.WIDTH, HistorianApplication.HEIGHT);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Database Deletion Confirmation");
        alert.setHeaderText("Warning! You are about to delete the database. This will erase all data.");
        alert.setContentText("Are you sure you want to delete the database?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // IF THEY CONFIRM DELETE
            homepageStage.setScene(scene);
        } else {
            // IF THEY CANCEL DELETE
            homepageStage.setScene(scene);
        }
    }
}
