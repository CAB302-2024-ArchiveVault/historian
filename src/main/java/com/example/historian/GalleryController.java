package com.example.historian;

import com.example.historian.auth.AuthSingleton;
import com.example.historian.models.account.Account;
import com.example.historian.models.photo.IPhotoDAO;
import com.example.historian.models.photo.Photo;
import com.example.historian.models.photo.SqlitePhotoDAO;
import com.example.historian.utils.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.example.historian.utils.StageManager.primaryStage;

public class GalleryController {
  @FXML
  private Button backButton;
  @FXML
  private Button forwardButton;
  @FXML
  private ImageView Image1;
  @FXML
  private ImageView Image2;
  @FXML
  private ImageView Image3;
  @FXML
  private ImageView Image4;
  @FXML
  private ImageView Image5;
  @FXML
  private ImageView Image6;
  @FXML
  private Text accountText;

  private int photoPage = 0;
  private AuthSingleton authSingleton;

  private IPhotoDAO photoDAO;
  private List<Photo> photoList;


  @FXML
  public void initialize() throws IOException {
    // Get the Auth Singleton
    authSingleton = AuthSingleton.getInstance();
    if (!authSingleton.checkAuthorised()) {
      StageManager.switchToHomepage();
    }

    Account authorisedAccount = authSingleton.getAccount();
    accountText.setText(authorisedAccount.getUsername());

    // Get the photo DAO
    photoDAO = new SqlitePhotoDAO();
    photoList = photoDAO.getAllPhotos();
    displayPhotos();
    buttonUpdate();
  }

  @FXML
  protected void onLogoutButtonClick() throws IOException {
    authSingleton.signOut();
    StageManager.switchToHomepage();
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

  protected void displayPhotos() {
    List<Photo> photosToDisplay = new ArrayList<>();

    for (int i = (photoPage * 6); i < Math.min((photoPage * 6) + 6, photoList.size()); i++) {
      photosToDisplay.add(photoList.get(i));
    }
    int imageCount = photosToDisplay.size();
    for (Photo photo : photosToDisplay) {
      displayPhoto(imageCount, photo);
      imageCount--;
    }
  }

  protected void displayPhoto(int index, Photo photo) {
    Image image = photo.getImage();

    switch (index) {
      case 1:
        Image1.setImage(image);
        break;
      case 2:
        Image2.setImage(image);
        break;
      case 3:
        Image3.setImage(image);
        break;
      case 4:
        Image4.setImage(image);
        break;
      case 5:
        Image5.setImage(image);
        break;
      case 6:
        Image6.setImage(image);
        break;
    }
  }

  protected void buttonUpdate() {
    backButton.setVisible(photoPage > 0);
    forwardButton.setVisible(photoList.size() > 6 && ((photoPage + 1) * 6) < photoList.size());
  }

  protected void clearImageViewers() {
    Image1.setImage(null);
    Image2.setImage(null);
    Image3.setImage(null);
    Image4.setImage(null);
    Image5.setImage(null);
    Image6.setImage(null);
  }

  @FXML
  protected void onBackButtonClick() {
    photoPage--;
    clearImageViewers();
    displayPhotos();
    buttonUpdate();
  }

  @FXML
  protected void onForwardButtonClick() {
    photoPage++;
    clearImageViewers();
    displayPhotos();
    buttonUpdate();
  }

  @FXML
  protected void onEditButtonClick() throws IOException {
    StageManager.switchScene("individualPhoto-view.fxml", 500, 600);
  }
}
