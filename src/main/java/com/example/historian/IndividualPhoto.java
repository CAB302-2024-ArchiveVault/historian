package com.example.historian;

import com.example.historian.auth.AuthSingleton;
import com.example.historian.models.account.Account;
import com.example.historian.models.account.AccountPrivilege;
import com.example.historian.models.location.ILocationDAO;
import com.example.historian.models.location.Location;
import com.example.historian.models.location.SqliteLocationDAO;
import com.example.historian.models.person.IPersonDAO;
import com.example.historian.models.person.Person;
import com.example.historian.models.person.SqlitePersonDAO;
import com.example.historian.models.photo.IPhotoDAO;
import com.example.historian.models.photo.Photo;
import com.example.historian.models.photo.SqlitePhotoDAO;
import com.example.historian.models.tag.ITagDAO;
import com.example.historian.models.tag.SqliteTagDAO;
import com.example.historian.models.tag.Tag;
import com.example.historian.utils.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.event.ActionEvent;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;


import java.util.Optional;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class IndividualPhoto {
    @FXML
    private Pane imagePane;
    @FXML
    private Label dateLabel;
    @FXML
    private TextField locationTextField;
    @FXML
    private Label locationLabel;
    @FXML
    private Label tagsLabel;
    @FXML
    private DatePicker myDatePicker;
    @FXML
    private Button locationButton;
    @FXML
    private Button tagButton;
    @FXML
    private Button editButton;
    @FXML
    private Button backButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button deleteButton;
    @FXML
    public ImageView imageDisplay;
    @FXML
    public HBox editOptionsHBox;
    @FXML
    public HBox tagModeHBox;
    @FXML
    public HBox tagOptionsHBox;
    @FXML
    public TextField firstNameTextField;
    @FXML
    public TextField lastNameTextField;

    public Photo selectedPhoto;
    public static int clickedImageId;
    private IPhotoDAO photoDAO;
    private Double xCoord;
    private Double yCoord;
    private Boolean tagModeOn = false;
    private Boolean viewTagsOn = false;
    private IPersonDAO personDAO;
    private ITagDAO tagDAO;
    private List<Tag> tags;
    private ILocationDAO locationDAO;

    private boolean editState = false;

    SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy");


    @FXML
    public void initialize() throws IOException{
        photoDAO = new SqlitePhotoDAO();
        personDAO = new SqlitePersonDAO();
        tagDAO = new SqliteTagDAO();
        locationDAO = new SqliteLocationDAO();
        selectedPhoto = photoDAO.getPhoto(clickedImageId);
        imageDisplay.setImage(selectedPhoto.getImage());

        Account currentUser = AuthSingleton.getInstance().getAccount();
        if (currentUser != null && currentUser.getAccountPrivilege() == AccountPrivilege.MEMBER) {
            // Hide the Edit button for members
            editButton.setVisible(false);
            editButton.setManaged(false);
        }

        if(photoDAO.getPhoto(clickedImageId).getDate() != null){
            String stringDate = formatter.format(selectedPhoto.getDate());
            //String myFormattedDate = photoDAO.getPhoto(clickedImageId).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            dateLabel.setText(stringDate);
        }

        if(photoDAO.getPhoto(clickedImageId).getLocation() != null){
            //String stringLocation = formatter.format(selectedPhoto.getLocation());
            locationLabel.setText(selectedPhoto.getLocation().getLocationName());
        }
        imageDisplay.setOnMouseClicked(this::handleImageViewClick);

        tags = selectedPhoto.getTagged();
    }

    private void handleImageViewClick(MouseEvent event) {
        if(!tagModeOn) {
            viewTagsOn = !viewTagsOn;
            viewTagMode();
        } else {
            removeAllCircles();
            xCoord = event.getX();
            yCoord = event.getY();

            Circle circle = new Circle(xCoord, yCoord, 5, Color.BLUE);
            circle.setStroke(Color.BLACK);
            circle.setStrokeWidth(2);
            imagePane.getChildren().add(circle);

            setTagOptionsVisible(true);
            firstNameTextField.setPromptText("First Name");
            lastNameTextField.setPromptText("Last Name");
            firstNameTextField.setText("First Name");
            lastNameTextField.setText("");
            firstNameTextField.requestFocus();
        }
    }

    private void viewTagMode() {
        if (viewTagsOn) {
            renderAllTags(false);
        } else {
            deleteAllRenderedTags();
        }
    }

    @FXML
    protected void onBackButtonClick() throws IOException {
        StageManager.switchScene("gallery-view.fxml");
    }

    @FXML
    public void getDate(ActionEvent event) {
        LocalDate myDate = myDatePicker.getValue();
        selectedPhoto.setDate(Date.from(myDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

    }
    @FXML
    public void getLocation(){
        String newLocationName = locationTextField.getText();
        Location newLocation = new Location(newLocationName);
        locationDAO.addLocation(newLocation);
        selectedPhoto.setLocation(newLocation);

    }

    @FXML
    public void onSaveButtonClick() {
        selectedPhoto.setTagged(tags);
        getLocation();
        photoDAO.updatePhoto(selectedPhoto);
        selectedPhoto = photoDAO.getPhoto(clickedImageId);
        tags = selectedPhoto.getTagged();

        editState = false;
        if (selectedPhoto.getDate() != null) {
            String stringDate = formatter.format(selectedPhoto.getDate());
            dateLabel.setText(stringDate);
        }
        if (selectedPhoto.getLocation() != null){
            //String stringLocation = formatter.format(selectedPhoto.getLocation());
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
    public void onEditButtonClick() throws IOException {
        editState = true;
        buttonUpdate();
    }
    @FXML
    public void onCancelButtonClick() throws IOException {
        editState = false;
        buttonUpdate();
    }

    public void buttonUpdate()  {
        myDatePicker.setVisible(editState);
        myDatePicker.setManaged(editState);
        saveButton.setVisible(editState);
        saveButton.setManaged(editState);
        locationTextField.setVisible(editState);
        locationTextField.setManaged(editState);
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
        viewTagsOn = false;
        viewTagMode();
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

    @FXML
    public void onTagButtonClick() throws IOException {
        tagModeOn = true;
        renderAllTags(true);
        setTagModeVisible();
    }

    @FXML
    public void onTagSaveButtonClick() throws IOException {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();

        Person person = new Person(firstName, lastName);

        Tag tag = new Tag(clickedImageId, person, xCoord.intValue(), yCoord.intValue());
        tags.add(tag);
        renderTag(tag, true);

        setTagOptionsVisible(false);
        removeAllCircles();
    }

    @FXML
    public void onTagBackButtonClick() throws IOException {
        tagModeOn = false;
        setTagModeVisible();
        deleteAllRenderedTags();
    }

    @FXML
    public void onTagCancelButtonClick() throws IOException {
        setTagOptionsVisible(false);
        removeAllCircles();
    }

    private void setTagModeVisible() {
        editOptionsHBox.setVisible(!tagModeOn);
        editOptionsHBox.setManaged(!tagModeOn);
        tagModeHBox.setVisible(tagModeOn);
        tagModeHBox.setManaged(tagModeOn);
    }

    private void setTagOptionsVisible(Boolean visible) {
        tagModeHBox.setVisible(!visible);
        tagModeHBox.setManaged(!visible);
        tagOptionsHBox.setVisible(visible);
        tagOptionsHBox.setManaged(visible);
    }

    private void deleteAllRenderedTags() {
        imagePane.getChildren().removeIf(node -> node instanceof StackPane);
        imagePane.getChildren().removeIf(node -> node instanceof Polygon);
    }

    private void renderTag(Tag tag, Boolean withX) {
        StackPane tagPane = new StackPane();

        int[] coordinates = tag.getCoordinates();
        double x = coordinates[0];
        double y = coordinates[1];

        Label label = new Label(" " + tag.getPerson().getFullName());

        label.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8);" +
                "-fx-background-radius: 15;" +
                "-fx-border-radius: 15;" +
                "-fx-border-color: rgba(0, 0, 0, 0.2);" +
                "-fx-padding: 5 10 5 10;" +
                "-fx-font-size: 14px;" +
                "-fx-font-family: Arial;" +
                "-fx-text-fill: black;");

        Polygon triangle = new Polygon();
        double triangleSize = 6;

        triangle.getPoints().addAll(
                x - triangleSize, y + triangleSize,
                x + triangleSize, y + triangleSize,
                x, y
        );
        triangle.setFill(Color.rgb(255, 255, 255, 0.8));
        triangle.setStroke(Color.rgb(0, 0, 0, 0.2));

        imagePane.getChildren().add(triangle);
        imagePane.getChildren().add(tagPane);

        tagPane.getChildren().add(label);

        double circleSize = 7.0;
        Circle circle = new Circle(circleSize);
        circle.setFill(Color.rgb(255, 255, 255, 0.8));
        circle.setStroke(Color.rgb(0, 0, 0, 0.2));
        Text closeText = new Text("X");
        closeText.setFill(Color.BLACK);

        StackPane closeButton = new StackPane(circle, closeText);

        closeButton.setOnMouseClicked(e -> {
            // THIS FUNCTION IS CALLED WHEN X BUTTON IS CLICKED
            imagePane.getChildren().removeAll(tagPane, triangle, closeButton);
            tags.remove(tag);
        });

        if (withX) {
            imagePane.getChildren().add(closeButton);
        }

        label.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            tagPane.setLayoutX(x - newBounds.getWidth() / 2);
            tagPane.setLayoutY(y + triangleSize);

            closeButton.setLayoutX(x - newBounds.getWidth() / 2 + newBounds.getWidth() - 10);
            closeButton.setLayoutY(y);
        });
    }

    private void renderAllTags(Boolean withX) {
        deleteAllRenderedTags();
        for (Tag tag : tags) {
            renderTag(tag, withX);
        }
    }

    private void removeAllCircles() {
        imagePane.getChildren().removeIf(node -> node instanceof Circle);
    }

}




