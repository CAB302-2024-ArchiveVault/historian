package com.example.historian;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HistorianApplication extends Application {

    //Default variables for titles and stage sizes
    public static final String APPTITLE = "Historian";
    public static final int WIDTH = 640;
    public static final int HEIGHT = 360;

    @Override
    public void start(Stage homepageStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HistorianApplication.class.getResource("homepage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        homepageStage.setTitle(APPTITLE);
        homepageStage.setScene(scene);
        homepageStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}