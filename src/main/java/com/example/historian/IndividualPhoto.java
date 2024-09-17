package com.example.historian;

import com.example.historian.models.photo.IPhotoDAO;
import com.example.historian.models.photo.Photo;
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
import javafx.scene.input.MouseEvent;
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
    private Button locationButton;
    @FXML
    private Button tagButton;
    @FXML
    private Button finishButton;
    @FXML
    private Button backButton;
    @FXML
    public ImageView imageDisplay;

    public Photo selectedPhoto;
    public static int clickedImageId;
    private IPhotoDAO photoDAO;
    private Double xCoord;
    private Double yCoord;
    private Boolean tagModeOn = false;

    @FXML
    public void initialize() throws IOException{
        photoDAO = new SqlitePhotoDAO();
        selectedPhoto = photoDAO.getPhoto(clickedImageId);
        imageDisplay.setImage(selectedPhoto.getImage());

        if(photoDAO.getPhoto(clickedImageId).getDate() != null){
            String myFormattedDate = photoDAO.getPhoto(clickedImageId).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            date.setText(myFormattedDate);
        }
        imageDisplay.setOnMouseClicked(this::handleImageViewClick);
    }

    private void handleImageViewClick(MouseEvent event) {
        xCoord = event.getX();
        yCoord = event.getY();

        System.out.println("Clicked at: (" + xCoord + ", " + yCoord + ")");
    }



    @FXML
    protected void onBackButtonClick() throws IOException {
        StageManager.switchScene("gallery-view.fxml");
    }

    @FXML
    public void getDate(ActionEvent event) {
        LocalDate myDate = myDatePicker.getValue();
        selectedPhoto.setDate(Date.from(myDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        String myFormattedDate = myDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        date.setText(myFormattedDate);
    }

    @FXML
    public void onfinishButtonClick() throws IOException {
        photoDAO.updatePhoto(selectedPhoto);
        StageManager.switchScene("gallery-view.fxml");
    }

    private void disableButtonsTagModeOn() {
        if(tagModeOn){
            locationButton.setDisable(true);
            backButton.setDisable(true);
            tagButton.setText("Cancel");
        }else {
            locationButton.setDisable(false);
            backButton.setDisable(false);
            tagButton.setText("Tag");
        }
    }

    @FXML
    public void onTagButtonClick() throws IOException {
        if (tagModeOn) {
            // TODO: NEED TO CANCEL THE OPERATION
        }
        tagModeOn = !tagModeOn;
        disableButtonsTagModeOn();
    }
}




