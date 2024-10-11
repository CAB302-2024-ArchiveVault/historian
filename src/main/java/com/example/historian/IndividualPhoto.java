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
import com.example.historian.models.tag.Tag;
import com.example.historian.utils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.stage.Screen;
import javafx.util.StringConverter;

import static com.example.historian.utils.StageManager.*;


public class IndividualPhoto {
  @FXML
  private Pane imagePane;
  @FXML
  private Label dateLabel;

  @FXML
  private ComboBox<Location> locationComboBox;
  @FXML
  private TextField newLocationTextField;
  @FXML
  private HBox existingLocationSelector;
  @FXML
  private HBox newLocationSelector;
  @FXML
  private Label locationLabel;

  @FXML
  private Label tagsLabel;
  @FXML
  private DatePicker myDatePicker;
  @FXML
  public ImageView imageDisplay;

  @FXML
  private HBox tagModeHBox;
  @FXML
  private VBox tagOptionsVBox;
  @FXML
  private HBox tagExistingPersonSelector;
  @FXML
  private HBox tagNewPersonSelector;
  @FXML
  private ComboBox<Person> personComboBox;
  @FXML
  private TextField firstNameTextField;
  @FXML
  private TextField lastNameTextField;

  @FXML
  private VBox imageInfo;
  @FXML
  private VBox editOptions;
  @FXML
  private HBox pageNavigation;
  @FXML
  private Button returnButton;
  @FXML
  private Button editButton;

  // Global data handlers
  private GallerySingleton gallerySingleton;
  public Photo selectedPhoto;
  private IPhotoDAO photoDAO;
  private ILocationDAO locationDAO;
  private IPersonDAO personDAO;

  // Page state
  private boolean isEditingTags = false;
  private boolean isViewingTags = false;


  // Tag editor
  private Double newTagXCoord;
  private Double newTagYCoord;
  private List<Tag> tempTags;
  private boolean isAddingNewPerson = false;

  // Misc
  SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy");
  private final int MAX_COMBOBOX_ITEMS = 10;
  private boolean isAddingNewLocation = false;


  @FXML
  public void initialize() throws IOException {
    gallerySingleton = GallerySingleton.getInstance();
    photoDAO = new SqlitePhotoDAO();
    locationDAO = new SqliteLocationDAO();
    personDAO = new SqlitePersonDAO();


    loadFirstPhotoFromQueue();

/*    if (gallerySingleton.isPhotoQueueEmpty()) {
      returnButton.setText("Return to gallery");
    } else {
      returnButton.setText("Next photo");
    }*/
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

    if (gallerySingleton.isPhotoQueueEmpty()) {
      returnButton.setText("Return to gallery");
    } else {
      returnButton.setText("Next photo");
    }

    primaryStage.setHeight(selectedPhoto.getAdjustedImageHeight() + 250);
    primaryStage.setWidth(selectedPhoto.getDefaultWidth() + 80);

    imageDisplay.setImage(selectedPhoto.getImage());
    imageDisplay.setOnMouseClicked(this::handleImageViewClick);

    imagePane.setTranslateX((double) ((selectedPhoto.getDefaultWidth() + 20) - (selectedPhoto.getAdjustedImageWidth())) /2);

    Date photoDate = selectedPhoto.getDate();
    if (photoDate != null) {
      String stringDate = formatter.format(photoDate);
      dateLabel.setText(stringDate);
      myDatePicker.setValue(
              photoDate.toInstant()
                      .atZone(ZoneId.systemDefault())
                      .toLocalDate()
      );
    } else {
      dateLabel.setText("Unknown");
      myDatePicker.setValue(null);
    }

    Location photoLocation = photoDAO.getPhoto(selectedPhoto.getId()).getLocation();
    initializeLocationComboBox(photoLocation);
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
    isViewingTags = false;
    viewTagMode();

    setPageEditMode(editMode);
    updateNewLocationSelectorVisibility(false);

    Account currentUser = AuthSingleton.getInstance().getAccount();
    if (currentUser != null) {
      // Check if the current user is a MEMBER
      if (currentUser.getAccountPrivilege() == AccountPrivilege.MEMBER) {
        // If the user is a member, only show the edit button if they uploaded the photo
        if (currentUser.getId() == selectedPhoto.getUploaderAccountId()) {
          editButton.setVisible(true);  // Show the Edit button
          editButton.setManaged(true);
        } else {
          editButton.setVisible(false);
          editButton.setManaged(false);
        }
      } else {
        // For all other privilege levels
        editButton.setVisible(true);
        editButton.setManaged(true);
      }
    }
  }







  private void initializeLocationComboBox(Location location) {
    AtomicBoolean isUpdatingLocationComboBox = new AtomicBoolean(false);

    // Collect a list of existing locations to display
    ObservableList<Location> locationList = FXCollections.observableArrayList(locationDAO.getAllLocations());
    FilteredList<Location> filteredLocations = new FilteredList<>(locationList, p -> true);
    locationComboBox.setItems(filteredLocations);

    // Custom string converter to display only the name of the location in the ComboBox
    locationComboBox.setConverter(new StringConverter<Location>() {
      @Override
      public String toString(Location location) {
        return location != null ? location.getLocationName() : null;
      }

      @Override
      public Location fromString(String s) {
        return locationList.stream()
                .filter(location -> location.getLocationName().equals(s))
                .findFirst()
                .orElse(null);
      }
    });

    // Handle the selection of an item in the ComboBox
    TextField locationField = locationComboBox.getEditor();
    locationField.setOnMouseClicked(event -> {
      locationComboBox.getSelectionModel().clearSelection();
      locationField.selectAll();
      locationComboBox.show();
    });
    locationField.textProperty().addListener((obs, oldValue, newValue) -> {
      if (isUpdatingLocationComboBox.get()) return;
      isUpdatingLocationComboBox.set(true);

      final String input = newValue.toLowerCase();
      if (locationComboBox.getSelectionModel().getSelectedItem() == null) {
        filteredLocations.setPredicate(l -> {
          if (input.isEmpty() || input.isBlank()) return true;
          return l.getLocationName().toLowerCase().contains(input);
        });
      }

      if (!filteredLocations.isEmpty()) {
        locationComboBox.setVisibleRowCount(Math.min(filteredLocations.size(), MAX_COMBOBOX_ITEMS));
        if (locationComboBox.isShowing()) {
          locationComboBox.hide();
          locationComboBox.show();
        }
      } else locationComboBox.hide();

      isUpdatingLocationComboBox.set(false);
    });

    // If a location is provided, select it by default
    if (location != null) {
      locationComboBox.getSelectionModel().select(location);
      locationField.setText(location.getLocationName());
    }
  }

  @FXML
  protected void showNewLocationSelector() {
    updateNewLocationSelectorVisibility(true);
  }

  @FXML
  protected void hideNewLocationSelector() {
    updateNewLocationSelectorVisibility(false);
  }

  private void updateNewLocationSelectorVisibility(boolean shown) {
    isAddingNewLocation = shown;
    existingLocationSelector.setVisible(!shown);
    existingLocationSelector.setManaged(!shown);
    newLocationSelector.setVisible(shown);
    newLocationSelector.setManaged(shown);
    newLocationTextField.setText(null);
  }


  private void initializePersonComboBox() {
    AtomicBoolean isUpdatingPersonComboBox = new AtomicBoolean(false);

    // Collect a list of existing people to display
    ObservableList<Person> personList = FXCollections.observableArrayList(personDAO.getAllPersons());
    FilteredList<Person> filteredPeople = new FilteredList<>(personList, p -> true);
    personComboBox.setItems(filteredPeople);

    // Custom string converter to display only the name of the person in the ComboBox
    personComboBox.setConverter(new StringConverter<Person>() {
      @Override
      public String toString(Person person) {
        return person != null ? person.getFullName() : null;
      }

      @Override
      public Person fromString(String s) {
        return personList.stream()
                .filter(p -> p.getFullName().equals(s))
                .findFirst()
                .orElse(null);
      }
    });

    // Handle the selection of an item in the ComboBox
    TextField personField = personComboBox.getEditor();
    personField.setOnMouseClicked(event -> {
      personComboBox.getSelectionModel().clearSelection();
      personField.selectAll();
      personComboBox.show();
    });
    personField.textProperty().addListener((obs, oldValue, newValue) -> {
      if (isUpdatingPersonComboBox.get()) return;
      isUpdatingPersonComboBox.set(true);

      final String input = newValue.toLowerCase();
      if (personComboBox.getSelectionModel().getSelectedItem() == null) {
        filteredPeople.setPredicate(p -> {
          if (input.isEmpty() || input.isBlank()) return true;
          return p.getFullName().toLowerCase().contains(input);
        });
      }

      if (!filteredPeople.isEmpty()) {
        personComboBox.setVisibleRowCount(Math.min(filteredPeople.size(), MAX_COMBOBOX_ITEMS));
        if (personComboBox.isShowing()) {
          personComboBox.hide();
          personComboBox.show();
        }
      } else personComboBox.hide();

      isUpdatingPersonComboBox.set(false);
    });
  }


  @FXML
  protected void showNewPersonSelector() {
    updateNewPersonSelectorVisibility(true);
    initializePersonComboBox();
  }

  @FXML
  protected void hideNewPersonSelector() {
    updateNewPersonSelectorVisibility(false);
  }

  private void updateNewPersonSelectorVisibility(boolean shown) {
    isAddingNewPerson = shown;
    tagExistingPersonSelector.setVisible(!shown);
    tagExistingPersonSelector.setManaged(!shown);
    tagNewPersonSelector.setVisible(shown);
    tagNewPersonSelector.setManaged(shown);
    firstNameTextField.setText(null);
    lastNameTextField.setText(null);
  }


  private void switchToGalleryScene() {
    try {
      switchScene("gallery-view.fxml", 1000, 900);
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
    if (isAddingNewLocation) {
      String locationName = newLocationTextField.getText();
      if (locationName == null || locationName.isEmpty() || locationName.isBlank()) return;

      Optional<Location> locationMatch = locationDAO.getAllLocations().stream()
              .filter(l -> l.getLocationName().equalsIgnoreCase(locationName))
              .findFirst();

      if (locationMatch.isPresent()) {
        selectedPhoto.setLocation(locationMatch.get());
      } else {
        Location newLocation = new Location(locationName);
        locationDAO.addLocation(newLocation);
        selectedPhoto.setLocation(newLocation);
      }
    } else {
      selectedPhoto.setLocation(locationComboBox.getSelectionModel().getSelectedItem());
    }
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
    if (isAddingNewPerson) {
      String firstName = firstNameTextField.getText();
      String lastName = lastNameTextField.getText();
      Person person = new Person(firstName, lastName);

      Tag tag = new Tag(selectedPhoto.getId(), person, newTagXCoord.intValue(), newTagYCoord.intValue());
      tempTags.add(tag);
      renderTag(tag, true);
    } else {
      Person person = personComboBox.getSelectionModel().getSelectedItem();
      Tag tag = new Tag(selectedPhoto.getId(), person, newTagXCoord.intValue(), newTagYCoord.intValue());
      tempTags.add(tag);
      renderTag(tag, true);
    }

    setTagOptionsVisible(false);
    removeAllCircles();
  }

  @FXML
  public void onTagBackButtonClick() throws IOException {
    isEditingTags = false;
    setTagModeVisible();
    deleteAllRenderedTags();

    isViewingTags = true;
    viewTagMode();
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
    tagOptionsVBox.setVisible(visible);
    tagOptionsVBox.setManaged(visible);
    hideNewPersonSelector();
    initializePersonComboBox();
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




