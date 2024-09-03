package com.example.historian;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import com.example.historian.utils.StageManager;

import java.io.IOException;

public class AccountManagementController {
    @FXML
    private Button exitButton;

    @FXML
    protected void onExitButtonClick() throws IOException {
        StageManager.switchScene("database-view.fxml");
    }
}
