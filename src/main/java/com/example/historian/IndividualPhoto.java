package com.example.historian;

import com.example.historian.models.photo.IPhotoDAO;
import com.example.historian.models.photo.SqlitePhotoDAO;
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

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.lang.Object;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import java.time.ZoneId;




public class IndividualPhoto {

    @FXML
    private Label date;
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
    public ImageView imageDisplay;

    public static Image clickedImage;
    public static int clickedImageId;
    private IPhotoDAO photoDAO;

    @FXML
    public void initialize() throws IOException{
        imageDisplay.setImage(clickedImage);
        photoDAO = new SqlitePhotoDAO();

        if(photoDAO.getPhoto(clickedImageId).getDate() != null){
            String myFormattedDate = photoDAO.getPhoto(clickedImageId).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            date.setText(myFormattedDate);
        }
    }

    @FXML
    protected void onBackButtonClick() throws IOException {
        StageManager.switchScene("gallery-view.fxml");
    }

    public void getDate(ActionEvent event) {

        photoDAO.getPhoto(clickedImageId).setDate(Date.from(myDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        LocalDate myDate = myDatePicker.getValue();
        String myFormattedDate = myDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        date.setText(myFormattedDate);

    }

}




