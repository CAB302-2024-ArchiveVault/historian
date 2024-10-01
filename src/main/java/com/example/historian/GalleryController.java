package com.example.historian;

import com.example.historian.auth.AuthSingleton;
import com.example.historian.models.account.Account;
import com.example.historian.models.account.AccountPrivilege;
import com.example.historian.models.photo.IPhotoDAO;
import com.example.historian.models.photo.Photo;
import com.example.historian.models.photo.SqlitePhotoDAO;
import com.example.historian.utils.GallerySingleton;
import com.example.historian.utils.StageManager;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.*;

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

  private int photosPerPage = 6;

  private AuthSingleton authSingleton;
  private GallerySingleton gallerySingleton;
  private IPhotoDAO photoDAO;
  public List<Photo> photoList;


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
    if (selectedFiles != null) {
      for (File selectedFile : selectedFiles) {
        try {
          int photoId = photoDAO.addPhoto(Photo.fromFile(selectedFile, ""));
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
      try {
        StageManager.switchScene("individualPhoto-view.fxml", 500, 600);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void displayPhotos() {
    List<Photo> photosToDisplay = new ArrayList<>();

    // Find all images to display
    for (int i = (gallerySingleton.getCurrentPage() * photosPerPage); i < Math.min((gallerySingleton.getCurrentPage() * photosPerPage) + photosPerPage, photoList.size()); i++) {
      photosToDisplay.add(photoList.get(i));
    }

    // Render photos
    imageContainer.getChildren().clear();
    for (int i = 0; i < photosToDisplay.size(); i++) {
      Photo photo = photosToDisplay.get(i);

      // Create the imageview
      ImageView imageView = new ImageView();
      imageView.setFitHeight(110.0);
      imageView.setFitWidth(135.0);
      imageView.setPickOnBounds(true);
      imageView.setPreserveRatio(true);
      imageView.setId(String.valueOf(photo.getId()));
      imageView.setOnMouseClicked(onImageClick());
      imageView.setImage(photo.getImage());

      // Set gridpane params
      imageContainer.add(imageView, i % 3, i / 3);
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
    forwardButton.setVisible(photoList.size() > 6 && ((gallerySingleton.getCurrentPage() + 1) * 6) < photoList.size());
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
