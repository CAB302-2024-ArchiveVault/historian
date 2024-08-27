package com.example.historian;

import com.example.historian.utils.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class DatabaseController {
    @FXML
    private Button exitButton;

    @FXML
    protected void onExitButtonClick() throws IOException {
        StageManager.switchToHomepage();
    }
}
