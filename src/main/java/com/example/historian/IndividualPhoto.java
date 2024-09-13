package com.example.historian;

import com.example.historian.utils.StageManager;
import com.example.historian.GalleryController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.time.format.DateTimeFormatter;
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
    private Label Date;
    @FXML
    private DatePicker myDatePicker;
    @FXML
    private Button Location;
    @FXML
    private Button Tag;
    @FXML
    private Button Finish;
    @FXML
    private Button Back;
    @FXML
    private ImageView imageDisplay;


    @FXML
    protected void onBackButtonClick() throws IOException {
        StageManager.switchScene("gallery-view.fxml");
    }

    public void getDate(ActionEvent event) {

        LocalDate myDate = myDatePicker.getValue();
        String myFormattedDate = myDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        Date.setText(myFormattedDate);
    }
}




