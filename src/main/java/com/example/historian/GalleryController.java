package com.example.historian;

import com.example.historian.auth.AuthSingleton;
import com.example.historian.models.account.Account;
import com.example.historian.models.account.AccountPrivilege;
import com.example.historian.models.photo.IPhotoDAO;
import com.example.historian.models.photo.Photo;
import com.example.historian.models.photo.SqlitePhotoDAO;
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
  private int photoPage = 0;

  private AuthSingleton authSingleton;
  private IPhotoDAO photoDAO;
  public List<Photo> photoList;


  @FXML
  public void initialize() throws IOException {
    // Get the Auth Singleton
    authSingleton = AuthSingleton.getInstance();
    if (!authSingleton.checkAuthorised()) {
      StageManager.switchToHomepage();
    }

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
          photoDAO.addPhoto(Photo.fromFile(selectedFile, "Temporary description!"));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    photoList = photoDAO.getAllPhotos();
    displayPhotos();
    buttonUpdate();
  }

  //Potentially redundant function, creates a second list to display images, used to avoid indexing issues


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

      // Get the ID of the clicked node
      int id = Integer.parseInt(clickedImage.getId());
      IndividualPhoto.clickedImageId = id;
      try {
        StageManager.switchScene("individualPhoto-view.fxml", 500, 600);
      } catch (Exception e) {
        e.printStackTrace();
      }
    };
  }


  public void buttonUpdate() {
    backButton.setVisible(photoPage > 0);
    forwardButton.setVisible(photoList.size() > 6 && ((photoPage + 1) * 6) < photoList.size());
  }

  @FXML
  public void onBackButtonClick() {
    photoPage--;
    displayPhotos();
    buttonUpdate();
  }

  @FXML
  public void onForwardButtonClick() {
    photoPage++;
    displayPhotos();
    buttonUpdate();
  }
}
