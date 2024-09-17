package com.example.historian;

import com.example.historian.models.photo.IPhotoDAO;
import com.example.historian.models.photo.Photo;
import com.example.historian.models.photo.SqlitePhotoDAO;
import com.example.historian.utils.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import javafx.scene.control.DatePicker;

import java.util.Optional;


public class IndividualPhoto {

    @FXML
    private Label dateLabel;
    @FXML
    private Label locationLabel;
    @FXML
    private Label tagsLabel;
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
            dateLabel.setText(stringDate);
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

    }

    @FXML
    public void onsaveButtonClick() {
        photoDAO.updatePhoto(selectedPhoto);
        editState = false;
        if (selectedPhoto.getDate() != null) {
            String stringDate = formatter.format(selectedPhoto.getDate());
            dateLabel.setText(stringDate);
        }
        if (selectedPhoto.getLocation() != null){
            locationLabel.setText(selectedPhoto.getLocation().getLocationName());
        }
        // Code to be implemented later once its determined how to display the tags in the label
        /*if (!selectedPhoto.getTagged().isEmpty())
        {
            tagLabel.setText(something in here);
        }
        */
        buttonUpdate();
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





