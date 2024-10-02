package com.example.historian;

import com.example.historian.models.location.ILocationDAO;
import com.example.historian.models.location.Location;
import com.example.historian.models.location.SqliteLocationDAO;
import com.example.historian.models.person.Person;
import com.example.historian.models.photo.IPhotoDAO;
import com.example.historian.models.photo.Photo;
import com.example.historian.models.photo.SqlitePhotoDAO;
import com.example.historian.models.tag.Tag;
import com.example.historian.utils.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
import java.util.stream.Collectors;

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
  public ImageView imageDisplay;
  @FXML
  public HBox tagModeHBox;
  @FXML
  public HBox tagOptionsHBox;
  @FXML
  public TextField firstNameTextField;
  @FXML
  public TextField lastNameTextField;

  @FXML
  private VBox imageInfo;
  @FXML
  private VBox editOptions;
  @FXML
  private HBox pageNavigation;

  // Global data handlers
  private GallerySingleton gallerySingleton;
  public Photo selectedPhoto;
  private IPhotoDAO photoDAO;
  private ILocationDAO locationDAO;

  // Page state
  private boolean isEditingTags = false;
  private boolean isViewingTags = false;

  // Tag editor
  private Double newTagXCoord;
  private Double newTagYCoord;
  private List<Tag> tempTags;

  // Misc
  SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy");


  @FXML
  public void initialize() throws IOException {
    gallerySingleton = GallerySingleton.getInstance();
    photoDAO = new SqlitePhotoDAO();
    locationDAO = new SqliteLocationDAO();

    loadFirstPhotoFromQueue();
  }

  private void loadFirstPhotoFromQueue() {
    GallerySingleton.PhotoQueueItem photoQueueItem = gallerySingleton.popFromPhotoQueue();
    loadPhoto(photoQueueItem.photoId(), photoQueueItem.openToEditMode());
  }

  private void loadPhoto(int photoId, boolean editMode) {
    selectedPhoto = photoDAO.getPhoto(photoId);
    if (selectedPhoto == null) {
      switchToGalleryScene();
    }

    imageDisplay.setImage(selectedPhoto.getImage());
    imageDisplay.setOnMouseClicked(this::handleImageViewClick);

    Date photoDate = selectedPhoto.getDate();
    if (photoDate != null) {
      String stringDate = formatter.format(photoDate);
      dateLabel.setText(stringDate);
    } else {
      dateLabel.setText("Unknown");
    }

    Location photoLocation = photoDAO.getPhoto(selectedPhoto.getId()).getLocation();
    if (photoLocation != null && !photoLocation.getLocationName().isEmpty()) {
      locationLabel.setText(photoLocation.getLocationName());
    } else {
      locationLabel.setText("Unknown");
    }

    tempTags = selectedPhoto.getTagged();
    if (!tempTags.isEmpty()) {
      tagsLabel.setText(tempTags.stream()
              .map(tag -> tag.getPerson().getFullName())
              .collect(Collectors.joining(", ")));
    } else {
      tagsLabel.setText("Nobody tagged");
    }

    setPageEditMode(editMode);
  }

  private void switchToGalleryScene() {
    try {
      StageManager.switchScene("gallery-view.fxml");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void handleImageViewClick(MouseEvent event) {
    if (!isEditingTags) {
      isViewingTags = !isViewingTags;
      viewTagMode();
    } else {
      removeAllCircles();
      newTagXCoord = event.getX();
      newTagYCoord = event.getY();

      Circle circle = new Circle(newTagXCoord, newTagYCoord, 5, Color.BLUE);
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
    if (isViewingTags) {
      renderAllTags(false);
    } else {
      deleteAllRenderedTags();
    }
  }

  @FXML
  protected void onBackButtonClick() throws IOException {
    // Check if there are more items in the display queue
    if (!gallerySingleton.isPhotoQueueEmpty()) {
      loadFirstPhotoFromQueue();
      return;
    }

    switchToGalleryScene();
  }

  @FXML
  public void getDate(ActionEvent event) {
    LocalDate myDate = myDatePicker.getValue();
    selectedPhoto.setDate(Date.from(myDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

  }

  @FXML
  public void getLocation() {
    String newLocationName = locationTextField.getText();
    if (newLocationName.isBlank() || newLocationName.isEmpty()) return;
    Location newLocation = new Location(newLocationName);
    locationDAO.addLocation(newLocation);
    selectedPhoto.setLocation(newLocation);
  }

  @FXML
  public void onSaveButtonClick() {

    selectedPhoto.setTagged(tempTags);
    getLocation();
    photoDAO.updatePhoto(selectedPhoto);

    // Check if the photo contains minimum necessary fields
    if (!selectedPhoto.hasMinimumFields()) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Unable to Save");
      alert.setWidth(400);
      alert.setHeight(400);
      alert.setContentText("This photo cannot be saved because it does not have a date, description, location or any tagged people. Please add at least one of those to save this photo.");
      alert.show();
      return;
    }

    loadPhoto(this.selectedPhoto.getId(), false);
  }

  @FXML
  public void onEditButtonClick() throws IOException {
    setPageEditMode(true);
  }

  @FXML
  public void onCancelButtonClick() throws IOException {
    setPageEditMode(false);
  }


  private void setPageEditMode(boolean isEditMode) {
    editOptions.setVisible(isEditMode);
    editOptions.setManaged(isEditMode);

    imageInfo.setVisible(!isEditMode);
    imageInfo.setManaged(!isEditMode);
    pageNavigation.setVisible(!isEditMode);
    pageNavigation.setManaged(!isEditMode);
  }


  @FXML
  private void deletePhoto() throws IOException {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Delete Photo");
    alert.setContentText("Are you sure you want to delete the photo?");
    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == ButtonType.OK) {
      photoDAO.removePhoto(photoDAO.getPhoto(selectedPhoto.getId()));
      switchToGalleryScene();
    }
  }

  @FXML
  public void onTagButtonClick() throws IOException {
    isEditingTags = true;
    renderAllTags(true);
    setTagModeVisible();
  }

  @FXML
  public void onTagSaveButtonClick() throws IOException {
    String firstName = firstNameTextField.getText();
    String lastName = lastNameTextField.getText();

    Person person = new Person(firstName, lastName);

    Tag tag = new Tag(selectedPhoto.getId(), person, newTagXCoord.intValue(), newTagYCoord.intValue());
    tempTags.add(tag);
    renderTag(tag, true);

    setTagOptionsVisible(false);
    removeAllCircles();
  }

  @FXML
  public void onTagBackButtonClick() throws IOException {
    isEditingTags = false;
    setTagModeVisible();
    deleteAllRenderedTags();
  }

  @FXML
  public void onTagCancelButtonClick() throws IOException {
    setTagOptionsVisible(false);
    removeAllCircles();
  }

  private void setTagModeVisible() {
    editOptions.setVisible(!isEditingTags);
    editOptions.setManaged(!isEditingTags);
    tagModeHBox.setVisible(isEditingTags);
    tagModeHBox.setManaged(isEditingTags);
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
      tempTags.remove(tag);
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
    for (Tag tag : tempTags) {
      renderTag(tag, withX);
    }
  }

  private void removeAllCircles() {
    imagePane.getChildren().removeIf(node -> node instanceof Circle);
  }

}




