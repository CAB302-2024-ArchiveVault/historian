package com.example.historian;

import com.example.historian.utils.StageManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class for the Historian application.
 */
public class HistorianApplication extends Application {

  //Default variables for titles and stage sizes
  private static final String APPTITLE = "Historian";

  @Override
  public void start(Stage homepageStage) throws IOException {
    try {
      StageManager.setPrimaryStage(homepageStage);
      StageManager.switchToHomepage();
      homepageStage.setTitle(APPTITLE);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    launch();
  }
}