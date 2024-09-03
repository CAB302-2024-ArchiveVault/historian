package com.example.historian;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;

public class AccountManagementController {
    @FXML
    private Button exitButton;

    @FXML
    protected void onExitButtonClick() throws IOException {
        Stage homepageStage = (Stage) exitButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HistorianApplication.class.getResource("database-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HistorianApplication.WIDTH, HistorianApplication.HEIGHT);
        homepageStage.setScene(scene);
    }
}
