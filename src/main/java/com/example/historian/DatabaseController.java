package com.example.historian;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class DatabaseController {
    @FXML
    private Button exitButton;

    @FXML
    protected void onexitButtonClick() throws IOException {
        Stage homepageStage = (Stage) exitButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HistorianApplication.class.getResource("homepage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HistorianApplication.WIDTH, HistorianApplication.HEIGHT);
        homepageStage.setScene(scene);
    }
}
