package com.example.historian;

import com.example.historian.auth.AuthSingleton;
import com.example.historian.models.account.Account;
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

  private int imagepage = 0;
  private AuthSingleton authSingleton;

  //Current method for storing the images, will be removed once DB functionality added
  public List<File> imageDatabase = new ArrayList<>();

  @FXML
  public void initialize() throws IOException {
    // Get the Auth Singleton
    authSingleton = AuthSingleton.getInstance();
    if (!authSingleton.checkAuthorised()) {
      StageManager.switchToHomepage();
    }

    Account authorisedAccount = authSingleton.getAccount();
    accountText.setText(authorisedAccount.getUsername());
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
        imageDatabase.addAll(selectedFiles);
    }
    displayPhotos();
    buttonUpdate();
  }

  //Potentially redundant function, creates a second list to display images, used to avoid indexing issues

  protected void displayPhotos() {

    List<File> displayImages = new ArrayList<>();

    for (int i=(imagepage*6); i < Math.min((imagepage*6)+6, imageDatabase.size()); i++)
    {
      displayImages.add(imageDatabase.get(i));
    }
    int imageCount = displayImages.size();
    for (File image: displayImages)
    {
        imageDisplay(imageCount,image);
        imageCount--;
    }
  }

  protected void imageDisplay(int imageCount, File image)
  {
    switch (imageCount)
    {
      case 1:
        Image image1 = new Image(image.toURI().toString());
        Image1.setImage(image1);
        break;
      case 2:
        Image image2 = new Image(image.toURI().toString());
        Image2.setImage(image2);
        break;
      case 3:
        Image image3 = new Image(image.toURI().toString());
        Image3.setImage(image3);
        break;
      case 4:
        Image image4 = new Image(image.toURI().toString());
        Image4.setImage(image4);
        break;
      case 5:
        Image image5 = new Image(image.toURI().toString());
        Image5.setImage(image5);
        break;
      case 6:
        Image image6 = new Image(image.toURI().toString());
        Image6.setImage(image6);
        break;
    }
  }

  protected void buttonUpdate()
  {
    backButton.setVisible(imagepage > 0);
    forwardButton.setVisible(imageDatabase.size() > 6 && ((imagepage + 1) * 6) < imageDatabase.size());
  }
  protected void clearImageViewers()
  {
    Image1.setImage(null);
    Image2.setImage(null);
    Image3.setImage(null);
    Image4.setImage(null);
    Image5.setImage(null);
    Image6.setImage(null);
  }
  @FXML
  protected void onBackButtonClick()
  {
    imagepage--;
    clearImageViewers();
    displayPhotos();
    buttonUpdate();
  }

  @FXML
  protected void onForwardButtonClick()
  {
    imagepage++;
    clearImageViewers();
    displayPhotos();
    buttonUpdate();
  }
}
