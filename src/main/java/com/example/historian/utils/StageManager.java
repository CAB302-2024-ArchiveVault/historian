package com.example.historian.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The StageManager class provides methods for managing the primary stage and switching scenes in a JavaFX application.
 */
public class StageManager {
  public static Stage primaryStage;
  public static final int WIDTH = 640;
  public static final int HEIGHT = 360;
  public static final String ABSOLUTE_RESOURCES_PATH = "/com/example/historian/";

  /**
   * Sets the primary stage for the application.
   *
   * @param stage the primary stage to set
   */
  public static void setPrimaryStage(Stage stage) {
    primaryStage = stage;
  }

  /**
   * Switches the scene of the primary stage to the specified FXML file with the given width and height.
   *
   * @param fxmlFilePath the path to the FXML file
   * @param width the width of the scene
   * @param height the height of the scene
   * @throws IOException if an error occurs during loading the FXML file
   */
  public static void switchScene(String fxmlFilePath, int width, int height) throws IOException {
    FXMLLoader loader = new FXMLLoader(StageManager.class.getResource(ABSOLUTE_RESOURCES_PATH + fxmlFilePath));
    Scene scene = new Scene(loader.load(), width, height);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /**
   * Switches the scene of the primary stage to the specified FXML file with default width and height.
   *
   * @param fxmlFilePath the path to the FXML file
   * @throws IOException if an error occurs during loading the FXML file
   */
  public static void switchScene(String fxmlFilePath) throws IOException {
    switchScene(fxmlFilePath, WIDTH, HEIGHT);
  }

  /**
   * Switches the scene of the primary stage to the homepage view.
   *
   * @throws IOException if an error occurs during loading the FXML file
   */
  public static void switchToHomepage() throws IOException {
    switchScene("homepage-view.fxml");
  }
}