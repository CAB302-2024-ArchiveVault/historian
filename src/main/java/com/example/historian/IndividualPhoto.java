package com.example.historian;

import com.example.historian.auth.AuthSingleton;
import com.example.historian.models.account.Account;
import com.example.historian.models.account.AccountPrivilege;
import com.example.historian.models.location.*;
import com.example.historian.models.person.*;
import com.example.historian.models.photo.*;
import com.example.historian.models.tag.Tag;
import com.example.historian.utils.*;
import static com.example.historian.utils.StageManager.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * Controller for the individual photo view.
 */
public class IndividualPhoto {
  @FXML private Pane imagePane;
  @FXML private Label dateLabel;
  @FXML private ComboBox<Location> locationComboBox;
  @FXML private TextField newLocationTextField;
  @FXML private HBox existingLocationSelector;
  @FXML private HBox newLocationSelector;
  @FXML private Label locationLabel;
  @FXML private TextField newDescriptionTextField;
  @FXML private Label descriptionLabel;
  @FXML private Label tagsLabel;
  @FXML private DatePicker myDatePicker;
  @FXML private ImageView imageDisplay;
  @FXML private HBox tagModeHBox;
  @FXML private VBox tagOptionsVBox;
  @FXML private HBox tagExistingPersonSelector;
  @FXML private HBox tagNewPersonSelector;
  @FXML private ComboBox<Person> personComboBox;
  @FXML private TextField firstNameTextField;
  @FXML private TextField lastNameTextField;
  @FXML private VBox imageInfo;
  @FXML private VBox editOptions;
  @FXML private HBox pageNavigation;
  @FXML private Button returnButton;
  @FXML private Button editButton;
  @FXML private Button deleteButton;

  // Global data handlers
  private GallerySingleton gallerySingleton;
  private Photo selectedPhoto;
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
  private void initialize() throws IOException {
    gallerySingleton = GallerySingleton.getInstance();
    photoDAO = new SqlitePhotoDAO();
    locationDAO = new SqliteLocationDAO();
    personDAO = new SqlitePersonDAO();



    loadFirstPhotoFromQueue();
  }

  private void loadFirstPhotoFromQueue() {
    GallerySingleton.PhotoQueueItem photoQueueItem = gallerySingleton.popFromPhotoQueue();

    loadPhoto(photoQueueItem.photoId(), photoQueueItem.openToEditMode());
  }

  private void loadPhoto(int photoId, boolean editMode) {
    selectedPhoto = photoDAO.getPhoto(photoId);

    if (selectedPhoto == null) {
      // Reset UI components since no photo is found
      dateLabel.setText("No date available");
      locationLabel.setText("No location available");
      descriptionLabel.setText("No description set");
      tagsLabel.setText("Nobody tagged");

      // Optionally handle the switch to the gallery scene
      switchToGalleryScene();
      return; // Exit early since no valid photo was loaded
    }


    if (gallerySingleton.isPhotoQueueEmpty()) {
      returnButton.setText("Return to gallery");
    } else {
      returnButton.setText("Next photo");
    }

    individualPhotoStage.setHeight(selectedPhoto.getAdjustedImageHeight() + 280);
    individualPhotoStage.setWidth(selectedPhoto.getDefaultWidth() + 80);

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
      dateLabel.setText("No date selected");
      myDatePicker.setValue(null);
    }

    Location photoLocation = photoDAO.getPhoto(selectedPhoto.getId()).getLocation();
    initializeLocationComboBox(photoLocation);
    if (photoLocation != null && !photoLocation.getLocationName().isEmpty()) {
      locationLabel.setText(photoLocation.getLocationName());
      newLocationTextField.setText(photoLocation.getLocationName());
    } else {
      locationLabel.setText("No location added");
      locationComboBox.setPromptText("Add location");
      newLocationTextField.setText(null);
    }

    String description = selectedPhoto.getDescription();
    if(description != null && !selectedPhoto.getDescription().isEmpty()){
      descriptionLabel.setText(selectedPhoto.getDescription());
      newDescriptionTextField.setText(selectedPhoto.getDescription());
    } else {
      descriptionLabel.setText("No description set");
      newDescriptionTextField.setText(null);
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
    } else {
      editButton.setVisible(false);
      editButton.setManaged(false);
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
  private void showNewLocationSelector() {
    updateNewLocationSelectorVisibility(true);
  }

  @FXML
  private void hideNewLocationSelector() {
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
  private void showNewPersonSelector() {
    updateNewPersonSelectorVisibility(true);
    initializePersonComboBox();
  }

  @FXML
  private void hideNewPersonSelector() {
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
      //displayPhotos();
      primaryStage.setIconified(false);
      individualPhotoStage.close();
      //switchScene("gallery-view.fxml", 1000, 900);
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
  private void onBackButtonClick() throws IOException {
    // Check if there are more items in the display queue
    descriptionLabel.setText(null);
    if (!gallerySingleton.isPhotoQueueEmpty()) {
      loadFirstPhotoFromQueue();
      return;
    }
    switchToGalleryScene();
  }

  @FXML
  private void getDate(ActionEvent event) {
    if (!(myDatePicker.getValue() == null))
    {
      LocalDate myDate = myDatePicker.getValue();
      selectedPhoto.setDate(Date.from(myDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }
  }

  @FXML
  private void getLocation() {
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
  private void getDescription(){
    String newDescription = newDescriptionTextField.getText();
    if (newDescription == null || newDescription.isEmpty() || newDescription.isBlank()) return;
    descriptionLabel.setText(selectedPhoto.getDescription());
    selectedPhoto.setDescription(newDescription);
  }

  @FXML
  private void onSaveButtonClick() {
    selectedPhoto.setTagged(tempTags);
    LocalDate myDate = myDatePicker.getValue();
    selectedPhoto.setDate(Date.from(myDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    getLocation();
    getDescription();
    photoDAO.updatePhoto(selectedPhoto);
    SharedProperties.imageUpdated.set(true);
    if (minimumFieldsCheck()) {
      loadPhoto(this.selectedPhoto.getId(), false);
    }
  }



  @FXML
  private void onEditButtonClick() throws IOException {
    setPageEditMode(true);
  }

  @FXML
  private void onCancelButtonClick() throws IOException {
    if(minimumFieldsCheck()) {
      setPageEditMode(false);
    }
  }

  private boolean minimumFieldsCheck()
  {
    if (!selectedPhoto.hasMinimumFields()) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Unable to Save");
      alert.setWidth(400);
      alert.setHeight(400);
      alert.setContentText("This photo cannot be saved because it does not have a date, description, location or any tagged people. Please add at least one of those to save this photo.");
      alert.show();
      return false;
    }
    else
    {
      return true;
    }
  }

  private void setPageEditMode(boolean isEditMode) {
    editOptions.setVisible(isEditMode);
    editOptions.setManaged(isEditMode);

    //Prevent selection of future date
    myDatePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
      @Override
      public javafx.scene.control.DateCell call(DatePicker datePicker) {
        return new javafx.scene.control.DateCell() {
          @Override
          public void updateItem(LocalDate item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && item.isAfter(LocalDate.now())) {
              setDisable(true); // Disable future dates
            }
          }
        };
      }
    });

    imageInfo.setVisible(!isEditMode);
    imageInfo.setManaged(!isEditMode);
    deleteButton.setVisible(isEditMode);
    returnButton.setVisible(!isEditMode);
  }


  @FXML
  private void deletePhoto() throws IOException {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Delete Photo");
    alert.setContentText("Are you sure you want to delete the photo?");
    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {

      // Create a new Stage for the progress indicator
      Stage progressStage = new Stage();
      progressStage.initModality(Modality.APPLICATION_MODAL); // Block input to other windows
      progressStage.setTitle("Deleting");

      // Create a ProgressIndicator and Label
      ProgressIndicator progressIndicator = new ProgressIndicator();
      Label label = new Label("Deleting your photo...");

      // Create a VBox to hold the progress indicator and label
      VBox vbox = new VBox(10, progressIndicator, label);
      vbox.setAlignment(Pos.CENTER);
      Scene scene = new Scene(vbox, 200, 100);
      progressStage.setScene(scene);
      progressStage.show(); // Show the progress window

      Task<Void> deleteTask = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
          // Perform the delete operation
          photoDAO.removePhoto(photoDAO.getPhoto(selectedPhoto.getId()));
          SharedProperties.imageUpdated.set(true); // Notify that image has been updated
          // Wait for imageUpdated to become false
          while (SharedProperties.imageUpdated.get())
          {
            Thread.sleep(100); // Sleep for a short time to avoid busy waiting
          }
          return null;
        }

        @Override
        protected void succeeded() {
          super.succeeded();
          // After the task has succeeded, switch to the gallery scene
          if(GallerySingleton.getInstance().isPhotoQueueEmpty())
          {
            switchToGalleryScene();
            progressStage.close();
          }
          else
          {
            loadFirstPhotoFromQueue();
            progressStage.close();
          }

        }

        @Override
        protected void failed() {
          super.failed();
          // Handle the error if needed
          Throwable exception = getException();
          System.err.println("Error deleting photo: " + exception.getMessage());
          progressStage.close();
        }
      };

      // Start the task in a new thread
      new Thread(deleteTask).start();

    }
  }

  @FXML
  private void onTagButtonClick() throws IOException {
    isEditingTags = true;
    renderAllTags(true);
    setTagModeVisible();
  }

  @FXML
  private void onTagSaveButtonClick() throws IOException {
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
  private void onTagBackButtonClick() throws IOException {
    isEditingTags = false;
    setTagModeVisible();
    deleteAllRenderedTags();

    isViewingTags = true;
    viewTagMode();
  }

  @FXML
  private void onTagCancelButtonClick() throws IOException {
    setTagOptionsVisible(false);
    removeAllCircles();
  }

  private void setTagModeVisible() {
    editOptions.setVisible(!isEditingTags);
    editOptions.setManaged(!isEditingTags);
    deleteButton.setVisible(!isEditingTags);
    deleteButton.setManaged(!isEditingTags);
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




