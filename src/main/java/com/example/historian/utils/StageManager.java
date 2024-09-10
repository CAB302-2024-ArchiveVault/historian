package com.example.historian.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StageManager {
  public static Stage primaryStage;
  public static final int WIDTH = 640;
  public static final int HEIGHT = 360;
  public static final String ABSOLUTE_RESOURCES_PATH = "/com/example/historian/";

  public static void setPrimaryStage(Stage stage) {
    primaryStage = stage;
  }

  public static void switchScene(String fxmlFilePath, int width, int height) throws IOException {
    FXMLLoader loader = new FXMLLoader(StageManager.class.getResource(ABSOLUTE_RESOURCES_PATH + fxmlFilePath));
    Scene scene = new Scene(loader.load(), width, height);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
  public static void switchScene(String fxmlFilePath) throws IOException {
    switchScene(fxmlFilePath, WIDTH, HEIGHT);
  }
  public static void switchToHomepage() throws IOException {
    switchScene("homepage-view.fxml");
  }
}
