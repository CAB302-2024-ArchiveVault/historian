package com.example.historian;

import com.example.historian.utils.StageManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.lang.Object;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;



public class IndividualPhoto {

    @FXML
    private Button Date;
    @FXML
    private Button Location;
    @FXML
    private Button Tag;
    @FXML
    private Button Finish;
    @FXML
    private Button Back;


    @FXML
    protected void onBackButtonClick() throws IOException {
        StageManager.switchScene("gallery-view.fxml");
    }


}