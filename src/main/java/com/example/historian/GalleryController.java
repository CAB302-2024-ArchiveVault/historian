package com.example.historian;

import com.example.historian.auth.AuthSingleton;
import com.example.historian.models.account.Account;
import com.example.historian.utils.StageManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;



import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static com.example.historian.utils.StageManager.primaryStage;

public class GalleryController {
  @FXML
  private Button logoutButton;
  @FXML
  private Button uploadPhotoButton;
  @FXML
  private Button viewButton;
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

  List<File> imageDatabase = new ArrayList<File>();

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
  protected void onUploadPhotoClick() throws IOException {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choose photo to upload");
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
    List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
    if (selectedFiles != null) {
        //imagesPane.getChildren().add(new ImageView(new Image(image.toURI().toString())));
        imageDatabase.addAll(selectedFiles);
    }
    displayPhotos();
    buttonUpdate();
  }

  protected void displayPhotos() throws IOException {

    List<File> displayImages = new ArrayList<File>();

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
    if (imagepage > 0)
    {
      backButton.setVisible(true);
    }
    if (imagepage == 0)
    {
      backButton.setVisible(false);
    }
    if (imageDatabase.size() > 6 && ((imagepage+1)*6) < imageDatabase.size() )
    {
      forwardButton.setVisible(true);
    }
    else
    {
      forwardButton.setVisible(false);
    }
  }
  @FXML
  protected void onBackButtonClick() throws IOException
  {
    imagepage--;
    Image1.setImage(null);
    Image2.setImage(null);
    Image3.setImage(null);
    Image4.setImage(null);
    Image5.setImage(null);
    Image6.setImage(null);
    displayPhotos();
    buttonUpdate();
  }

  @FXML
  protected void onForwardButtonClick() throws IOException
  {
    imagepage++;
    Image1.setImage(null);
    Image2.setImage(null);
    Image3.setImage(null);
    Image4.setImage(null);
    Image5.setImage(null);
    Image6.setImage(null);
    displayPhotos();
    buttonUpdate();
  }


  //ObservableList<Node> imageViewers = imagesPane.getChildren();
  //for (Node viewer: imageViewers)
  //{
  //  viewer.bind(1.0);
  // viewer.maxWidth(1.0);
  //}

  //Image image = new Image(selectedFile.toURI().toString(), true);
  //ImageView iv1 = new ImageView();
  //imageViewerTest.setImage(image);
  //imageViewerTest.getImage();

}
