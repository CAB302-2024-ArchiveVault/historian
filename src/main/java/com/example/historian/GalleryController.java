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
import com.example.historian.utils.GallerySingleton;
import com.example.historian.utils.GallerySingleton;
import com.example.historian.utils.StageManager;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import javafx.scene.control.Label;

import javax.swing.event.ChangeListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.example.historian.utils.StageManager.primaryStage;

public class GalleryController {
  @FXML
  public GridPane imageContainer;
  @FXML
  public Button backButton;
  @FXML
  public Button forwardButton;
  @FXML
  public Text accountText;
  @FXML
  public Button logoutButton;
  @FXML
  public DatePicker fromDateFilter;
  @FXML
  public DatePicker toDateFilter;
  @FXML
  public ComboBox<Location> locationFilterComboBox;
  @FXML
  public ComboBox<Person> personFilterComboBox;
  @FXML
  public Button applyFilterButton;

  private int photosPerPage = 12;
  private int photosPerRow = 4;
  private int photoPage = 0;

  private AuthSingleton authSingleton;
  private GallerySingleton gallerySingleton;
  private IPhotoDAO photoDAO;
  public List<Photo> photoList;

  SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

  Image tagImage = new Image("file:src/icons/tag-removebg-preview.png");

  Scene thisScene = primaryStage.getScene();

  private ILocationDAO locationDAO;
  private ObservableList<Location> locationList;
  private IPersonDAO personDAO;
  private ObservableList<Person> personList;


  @FXML
  public void initialize() throws IOException {
    // Get the Auth Singleton
    authSingleton = AuthSingleton.getInstance();
    if (!authSingleton.checkAuthorised()) {
      StageManager.switchToHomepage();
    }

    // Get the gallery singleton
    gallerySingleton = GallerySingleton.getInstance();

    Account authorisedAccount = authSingleton.getAccount();
    accountText.setText(authorisedAccount.getUsername());
    if (authorisedAccount.getAccountPrivilege() == AccountPrivilege.DATABASE_OWNER) {
      logoutButton.setText("Back");
    }


    // Get the photo DAO
    photoDAO = new SqlitePhotoDAO();
    photoList = photoDAO.getAllPhotos();
    displayPhotos();
    buttonUpdate();

    // Setup location ComboBox
    locationDAO = new SqliteLocationDAO();
    AtomicBoolean updatingLocationComboBox = new AtomicBoolean(false);
    locationList = FXCollections.observableArrayList(locationDAO.getAllLocations());
    FilteredList<Location> filteredItemsLocation = new FilteredList<>(locationList, p -> true);
    locationFilterComboBox.setItems(filteredItemsLocation);
    // Custom StringConverter to display only the location name in the ComboBox
    locationFilterComboBox.setConverter(new StringConverter<Location>() {
      @Override
      public String toString(Location location) {
        return location != null ? location.getLocationName() : "";
      }

      @Override
      public Location fromString(String string) {
        return locationList.stream().filter(location -> location.getLocationName().equals(string)).findFirst().orElse(null);
      }
    });

    TextField locationEditor = locationFilterComboBox.getEditor();
    locationEditor.setOnMouseClicked(event -> {
      locationFilterComboBox.getSelectionModel().clearSelection();
      locationEditor.selectAll();
      if (!locationFilterComboBox.isShowing()) {
        locationFilterComboBox.show();
      }
    });
    locationEditor.textProperty().addListener((obs, oldValue, newValue) -> {
      if (updatingLocationComboBox.get()) {
        return;
      }
      updatingLocationComboBox.set(true);

      final String input = newValue.toLowerCase();

      if (locationFilterComboBox.getSelectionModel().getSelectedItem() == null) {
        filteredItemsLocation.setPredicate(location -> {
          if (input == null || input.isEmpty()) {
            return true;
          }
          return location.getLocationName().toLowerCase().contains(input);
        });
      }

      if (!filteredItemsLocation.isEmpty()) {
        int size = filteredItemsLocation.size();
        int maxVisibleRows = 10; // You can adjust this limit
        locationFilterComboBox.setVisibleRowCount(Math.min(size, maxVisibleRows));
        if (locationFilterComboBox.isShowing()) {
          locationFilterComboBox.hide();
          locationFilterComboBox.show();
        }
      } else {
        locationFilterComboBox.hide();
      }

      updatingLocationComboBox.set(false);
    });

    locationFilterComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldLocation, newLocation) -> {
      if (newLocation != null) {
        applyFilterButton.requestFocus();
      }
    });

    // Setup person ComboBox
    personDAO = new SqlitePersonDAO();
    AtomicBoolean updatingPersonComboBox = new AtomicBoolean(false);
    personList = FXCollections.observableArrayList(personDAO.getAllPersons());
    FilteredList<Person> filteredItemsPerson = new FilteredList<>(personList, p -> true);
    personFilterComboBox.setItems(filteredItemsPerson);
    personFilterComboBox.setConverter(new StringConverter<Person>() {
      @Override
      public String toString(Person person) {
        return person != null ? person.getFullName() : "";
      }

      @Override
      public Person fromString(String string) {
        return personList.stream().filter(person -> person.getFullName().equals(string)).findFirst().orElse(null);
      }
    });

    TextField personEditor = personFilterComboBox.getEditor();
    personEditor.setOnMouseClicked(event -> {
      personFilterComboBox.getSelectionModel().clearSelection();
      personEditor.selectAll();
      if (!personFilterComboBox.isShowing()) {
        personFilterComboBox.show();
      }
    });
    personEditor.textProperty().addListener((obs, oldValue, newValue) -> {
      if (updatingPersonComboBox.get()) {
        return;
      }
      updatingPersonComboBox.set(true);

      final String input = newValue.toLowerCase();

      if (personFilterComboBox.getSelectionModel().getSelectedItem() == null) {
        filteredItemsPerson.setPredicate(person -> {
          if (input == null || input.isEmpty()) {
            return true;
          }
          return person.getFullName().toLowerCase().contains(input);
        });
      }

      if (!filteredItemsPerson.isEmpty()) {
        int size = filteredItemsPerson.size();
        int maxVisibleRows = 10; // You can adjust this limit
        personFilterComboBox.setVisibleRowCount(Math.min(size, maxVisibleRows));
        if (personFilterComboBox.isShowing()) {
          personFilterComboBox.hide();
          personFilterComboBox.show();
        }
      } else {
        personFilterComboBox.hide();
      }

      updatingPersonComboBox.set(false);
    });

    personFilterComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldPerson, newPerson) -> {
      if (newPerson != null) {
        applyFilterButton.requestFocus();
      }
    });

  }

  @FXML
  protected void onLogoutButtonClick() throws IOException {
    Account authorisedAccount = authSingleton.getAccount();
    if (authorisedAccount.getAccountPrivilege() == AccountPrivilege.DATABASE_OWNER) {
      StageManager.switchScene("admin-options-view.fxml");
    } else {
      authSingleton.signOut();
      StageManager.switchToHomepage();
    }
  }

  @FXML
  protected void onUploadPhotoClick() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choose photo/s to upload");
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
    List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);

    // Get the current authenticated user
    Account currentUser = AuthSingleton.getInstance().getAccount();
    int uploaderAccountId = currentUser != null ? currentUser.getId() : -1;  // Assign uploader's account ID


    if (selectedFiles != null) {
      for (File selectedFile : selectedFiles) {
        try {
          Photo newPhoto = Photo.fromFile(selectedFile, "");
          newPhoto.setUploaderAccountId(uploaderAccountId);
          int photoId = photoDAO.addPhoto(newPhoto);
          gallerySingleton.addToPhotoQueue(new GallerySingleton.PhotoQueueItem(photoId, true));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    // Update the display
    photoList = photoDAO.getAllPhotos();
    displayPhotos();
    buttonUpdate();

    // Check whether the individual photo view needs to be opened
    checkToDisplayIndividualPhoto();
  }

  private void checkToDisplayIndividualPhoto() {
    if (!gallerySingleton.isPhotoQueueEmpty()) {

      double originalWidth = photoDAO.getPhoto(gallerySingleton.firstPhotoInQueueID()).getImage().getWidth();
      double originalHeight = photoDAO.getPhoto(gallerySingleton.firstPhotoInQueueID()).getImage().getHeight();
      double maxWidth = 500;
      double maxHeight = 500;
      double aspectRatio = originalWidth/originalHeight;

      double newHeight;

      if (aspectRatio > 1) {
        // Image is wider than it is tall
        newHeight = maxWidth / aspectRatio;
      }
      else if (aspectRatio == 1)
      {
        newHeight = maxHeight;
      }
      else {
        // Image is taller than it is wide
        newHeight = maxHeight;
      }


      try {
        //StageManager.switchScene("individualPhoto-view.fxml", 540, 630);
        StageManager.switchScene("individualPhoto-view.fxml", 580, (int) (newHeight + 200));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void displayPhotos() {
    List<Photo> photosToDisplay = new ArrayList<>();

    // Find all images to display
    for (int i = (photoPage * photosPerPage); i < Math.min((photoPage * photosPerPage) + photosPerPage, photoList.size()); i++) {
      photosToDisplay.add(photoList.get(i));
    }


    // Render photos
    imageContainer.getChildren().clear();
    for (int i = 0; i < photosToDisplay.size(); i++) {
      Photo photo = photosToDisplay.get(i);

      //Create the VBox
      VBox vbox = new VBox();

      // Create the imageview
      ImageView imageView = new ImageView();
      imageView.setPreserveRatio(false);
      imageView.setFitHeight(200.0);
      imageView.setFitWidth(200.0);
      imageView.setPickOnBounds(true);
      imageView.setId(String.valueOf(photo.getId()));
      imageView.setOnMouseClicked(onImageClick());
      imageView.setImage(photo.getImage());


      //Create the hbox to store the location and date label
      HBox hbox = new HBox(8);
      hbox.setAlignment(Pos.TOP_LEFT);


      //Create the location label
      Label LocationLabel  = new Label();

      if(photo.getLocation() != null){
        LocationLabel.setText(photo.getLocation().getLocationName());
      }

      //Create the date label
      Label DateLabel = new Label();

      if(photo.getDate() != null){
        String stringDate = formatter.format(photo.getDate());
        //String myFormattedDate = photoDAO.getPhoto(clickedImageId).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        DateLabel.setText(stringDate);
      }
      // Create the stackpane
      StackPane tagStack = new StackPane();


      // Set gridpane params
      imageContainer.add(vbox, i % photosPerRow, i / photosPerRow);
      vbox.getChildren().add(tagStack);
      tagStack.getChildren().add(imageView);
      StackPane.setAlignment(imageView,Pos.TOP_LEFT);
      vbox.getChildren().add(hbox);
      hbox.getChildren().add(LocationLabel);
      hbox.getChildren().add(DateLabel);

      if(!photo.getTagged().isEmpty())
      {
        ImageView tagView = new ImageView();
        tagView.setFitHeight(23);
        tagView.setFitWidth(23);
        tagView.setSmooth(true);
        tagView.setPreserveRatio(true);
        //Image tagImage = new Image("file:tag.jpg");
        tagView.setImage(tagImage);
        //imageView.setImage(tagImage);
        StackPane.setAlignment(tagView,Pos.BOTTOM_LEFT);
        tagStack.getChildren().add(tagView);
      }
    }
  }

  protected EventHandler<? super MouseEvent> onImageClick() {
    return event -> {
      // Get the source of the event (the clicked node)
      ImageView clickedImage = (ImageView) event.getSource();

      // Add this image to the photo queue, then open the individual photo page
      gallerySingleton.addToPhotoQueue(
          new GallerySingleton.PhotoQueueItem(Integer.parseInt(clickedImage.getId()), false)
      );
      checkToDisplayIndividualPhoto();
    };
  }


  public void buttonUpdate() {
    backButton.setVisible(gallerySingleton.getCurrentPage() > 0);
    forwardButton.setVisible(photoList.size() > photosPerPage && ((gallerySingleton.getCurrentPage() + 1) * photosPerPage) < photoList.size());
  }

  private void disableDates(boolean afterDate, LocalDate cutOffDate, DatePicker datePicker) {
    datePicker.setDayCellFactory(picker -> new DateCell() {
      public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);

        if (afterDate) {
          setDisable(empty || date.compareTo(cutOffDate) > 0 );
        } else {
          setDisable(empty || date.compareTo(cutOffDate) < 0 );
        }
      }
    });
  }

  private void enableAllDates(DatePicker datePicker) {
    datePicker.setDayCellFactory(picker -> new DateCell() {
      public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);
        setDisable(false);
      }
    });
  }

  @FXML
  public void onFromDateFilterChange() {
    if (toDateFilter.getValue() == null) {
      enableAllDates(toDateFilter);
    } else {
      if (toDateFilter.getValue() != null) {
        if (toDateFilter.getValue().compareTo(fromDateFilter.getValue()) < 0) {
          toDateFilter.setValue(null);
        }
      }
      disableDates(false, fromDateFilter.getValue(), toDateFilter);
    }
  }

  @FXML
  public void onToDateFilterChange() {
    if (toDateFilter.getValue() == null) {
      enableAllDates(fromDateFilter);
    } else {
      if (fromDateFilter.getValue() != null) {
        if (fromDateFilter.getValue().compareTo(toDateFilter.getValue()) > 0) {
          fromDateFilter.setValue(null);
        }
      }
      disableDates(true, toDateFilter.getValue(), fromDateFilter);
    }
  }

  @FXML
  public void onApplyFilterButtonClick() {
    Location selectedLocation = locationFilterComboBox.getSelectionModel().getSelectedItem();
    int selectedLocationId = selectedLocation != null ? selectedLocation.getId() : -1;
    Person selectedPerson = personFilterComboBox.getSelectionModel().getSelectedItem();
    int selectedPersonId = selectedPerson != null ? selectedPerson.getId() : -1;
    LocalDate fromLocalDate = fromDateFilter.getValue();
    Date fromDate = fromLocalDate != null ? Date.from(fromLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
    LocalDate toLocalDate = toDateFilter.getValue();
    Date toDate = toLocalDate != null ? Date.from(toLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;

    photoList = photoDAO.getPhotosByFilter(fromDate, toDate, selectedLocationId, selectedPersonId);
    displayPhotos();
    buttonUpdate();
  }

  @FXML
  public void onBackButtonClick() {
    gallerySingleton.setCurrentPage(gallerySingleton.getCurrentPage() - 1);
    displayPhotos();
    buttonUpdate();
  }

  @FXML
  public void onForwardButtonClick() {
    gallerySingleton.setCurrentPage(gallerySingleton.getCurrentPage() + 1);
    displayPhotos();
    buttonUpdate();
  }
}
