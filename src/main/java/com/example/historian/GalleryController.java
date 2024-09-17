package com.example.historian;

import com.example.historian.auth.AuthSingleton;
import com.example.historian.models.account.Account;
import com.example.historian.models.account.AccountPrivilege;
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
  public Button backButton;
  @FXML
  public Button forwardButton;
  @FXML
  public ImageView Image1;
  @FXML
  public ImageView Image2;
  @FXML
  public ImageView Image3;
  @FXML
  public ImageView Image4;
  @FXML
  public ImageView Image5;
  @FXML
  public ImageView Image6;
  @FXML
  public Text accountText;
  @FXML
  public Button logoutButton;


  int image1Id;
  int image2Id;
  int image3Id;
  int image4Id;
  int image5Id;
  int image6Id;

  public int photoPage = 0;
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
        image1Id = photo.getId();
        break;
      case 2:
        Image2.setImage(image);
        image2Id = photo.getId();
        break;
      case 3:
        Image3.setImage(image);
        image3Id = photo.getId();
        break;
      case 4:
        Image4.setImage(image);
        image4Id = photo.getId();
        break;
      case 5:
        Image5.setImage(image);
        image5Id = photo.getId();
        break;
      case 6:
        Image6.setImage(image);
        image6Id = photo.getId();
        break;
    }
  }


  public void buttonUpdate() {
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

  public void onBackButtonClick() {
    photoPage--;
    clearImageViewers();
    displayPhotos();
    buttonUpdate();
  }

  @FXML
  public void onForwardButtonClick() {
    photoPage++;
    clearImageViewers();
    displayPhotos();
    buttonUpdate();
  }

  @FXML
  protected void onEditButtonClick() throws IOException {
    StageManager.switchScene("individualPhoto-view.fxml", 500, 600);
  }

  @FXML
  protected void onImage1Click() throws IOException
  {
      IndividualPhoto.clickedImageId = image1Id;
      StageManager.switchScene("individualPhoto-view.fxml", 500, 600);
  }

  @FXML
  protected void onImage2Click() throws IOException
  {
    IndividualPhoto.clickedImageId = image2Id;
    StageManager.switchScene("individualPhoto-view.fxml", 500, 600);
  }

  @FXML
  protected void onImage3Click() throws IOException
  {
    IndividualPhoto.clickedImageId = image3Id;
    StageManager.switchScene("individualPhoto-view.fxml", 500, 600);
  }

  @FXML
  protected void onImage4Click() throws IOException
  {
    IndividualPhoto.clickedImageId = image4Id;
    StageManager.switchScene("individualPhoto-view.fxml", 500, 600);
  }

  @FXML
  protected void onImage5Click() throws IOException
  {
    IndividualPhoto.clickedImageId = image5Id;
    StageManager.switchScene("individualPhoto-view.fxml", 500, 600);
  }

  @FXML
  protected void onImage6Click() throws IOException
  {
    IndividualPhoto.clickedImageId = image6Id;
    StageManager.switchScene("individualPhoto-view.fxml", 500, 600);
  }


}
