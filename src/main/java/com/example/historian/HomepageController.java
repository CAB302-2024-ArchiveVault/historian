package com.example.historian;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class HomepageController {

    @FXML
    private Button loginButton;

    @FXML
    private Button dataBaseButton;

    //When the database button is clicked, creates and swaps to the database stage + scene
    @FXML
    protected void ondataBaseButtonClick() throws IOException {
        Stage databaseStage = (Stage) dataBaseButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HistorianApplication.class.getResource("database-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HistorianApplication.WIDTH, HistorianApplication.HEIGHT);
        databaseStage.setScene(scene);
    }

    @FXML
    protected void onloginButtonClick() throws IOException {
        Stage galleryStage = (Stage) loginButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HistorianApplication.class.getResource("gallery-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HistorianApplication.WIDTH, HistorianApplication.HEIGHT);
        galleryStage.setScene(scene);
    }
}
