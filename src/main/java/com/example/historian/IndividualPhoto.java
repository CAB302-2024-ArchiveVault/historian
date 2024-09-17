package com.example.historian;

import com.example.historian.models.photo.IPhotoDAO;
import com.example.historian.models.photo.Photo;
import com.example.historian.models.photo.SqlitePhotoDAO;
import com.example.historian.utils.StageManager;
import com.example.historian.GalleryController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import javafx.scene.control.DatePicker;
import java.time.ZoneId;
import java.util.Optional;


public class IndividualPhoto {

    @FXML
    private Label date;
    @FXML
    private DatePicker myDatePicker;
    @FXML
    private Button locationButton;
    @FXML
    private Button editButton;
    @FXML
    private Button backButton;
    @FXML
    private Button tagButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button Back;
    @FXML
    private Button deleteButton;
    @FXML
    public ImageView imageDisplay;

    public Photo selectedPhoto;
    public static int clickedImageId;
    private IPhotoDAO photoDAO;

    private boolean editState = false;

    SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy");


    @FXML
    public void initialize() throws IOException{
        photoDAO = new SqlitePhotoDAO();
        selectedPhoto = photoDAO.getPhoto(clickedImageId);
        imageDisplay.setImage(selectedPhoto.getImage());

        if(photoDAO.getPhoto(clickedImageId).getDate() != null){
            String stringDate = formatter.format(selectedPhoto.getDate());
            //String myFormattedDate = photoDAO.getPhoto(clickedImageId).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            date.setText(stringDate);
        }
    }

    @FXML
    protected void onbackButtonClick() throws IOException {
        StageManager.switchScene("gallery-view.fxml");
    }

    @FXML
    public void getDate(ActionEvent event) {
        LocalDate myDate = myDatePicker.getValue();
        selectedPhoto.setDate(Date.from(myDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));



        //myFormattedDate = myDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        //String stringDate = formatter.format(selectedPhoto.getDate());
        //date.setText(stringDate);

        //myFormattedDate = selectedPhoto.getDate().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @FXML
    public void onsaveButtonClick() throws IOException {
        photoDAO.updatePhoto(selectedPhoto);
        editState = false;
        String stringDate = formatter.format(selectedPhoto.getDate());
        date.setText(stringDate);
        buttonUpdate();
        //StageManager.switchScene("gallery-view.fxml");
    }

    @FXML
    public void oneditButtonClick() throws IOException {
        editState = true;
        buttonUpdate();
    }
    @FXML
    public void oncancelButtonClick() throws IOException {
        editState = false;
        buttonUpdate();
    }

    public void buttonUpdate()  {
        myDatePicker.setVisible(editState);
        myDatePicker.setManaged(editState);
        saveButton.setVisible(editState);
        saveButton.setManaged(editState);
        locationButton.setVisible(editState);
        locationButton.setManaged(editState);
        tagButton.setVisible(editState);
        tagButton.setManaged(editState);
        cancelButton.setVisible(editState);
        cancelButton.setManaged(editState);
        deleteButton.setVisible(editState);
        deleteButton.setManaged(editState);
        backButton.setVisible(!editState);
        backButton.setManaged(!editState);
        editButton.setVisible(!editState);
        editButton.setManaged(!editState);
    }
    @FXML
    private void deletePhoto() throws IOException
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Photo");
        alert.setContentText("Are you sure you want to delete the photo?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            photoDAO.removePhoto(photoDAO.getPhoto(clickedImageId));
            StageManager.switchScene("gallery-view.fxml");
        }
    }
    }





