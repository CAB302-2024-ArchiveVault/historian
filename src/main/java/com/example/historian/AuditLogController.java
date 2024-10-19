package com.example.historian;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;
import com.example.historian.utils.StageManager;

/**
 * Controller for the audit log view.
 */
public class AuditLogController {
    @FXML private Button exitButton;

    @FXML
    protected void onExitButtonClick() throws IOException {
        StageManager.switchScene("database-view.fxml");
    }
}
