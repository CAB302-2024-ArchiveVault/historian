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
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.scene.control.Label;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
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

  private int photosPerPage = 12;
  private int photosPerRow = 4;
  private int photoPage = 0;

  private AuthSingleton authSingleton;
  private IPhotoDAO photoDAO;
  public List<Photo> photoList;

  SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

  Image tagImage = new Image("file:src/icons/tag-removebg-preview.png");


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

      //Create the VBox
      VBox vbox = new VBox();

      // Create the imageview
      ImageView imageView = new ImageView();
      imageView.setFitHeight(100.0);
      imageView.setFitWidth(135.0);
      imageView.setPickOnBounds(true);
      imageView.setPreserveRatio(false);

      //Rectangle2D viewportRect = new Rectangle2D(0, 0, 135, 100);
      //imageView.setViewport(viewportRect);

      imageView.setId(String.valueOf(photo.getId()));
      imageView.setOnMouseClicked(onImageClick());
      imageView.setImage(photo.getImage());


      //Create the hbox to store the location and date label
      HBox hbox = new HBox(8);
      hbox.setAlignment(Pos.TOP_CENTER);


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
      vbox.getChildren().add(hbox);
      hbox.getChildren().add(LocationLabel);
      hbox.getChildren().add(DateLabel);

      if(!photo.getTagged().isEmpty())
      {
        ImageView tagView = new ImageView();
        tagView.setFitHeight(30);
        tagView.setFitWidth(30);
        tagView.setPreserveRatio(true);
        //Image tagImage = new Image("file:tag.jpg");
        tagView.setImage(tagImage);
        //imageView.setImage(tagImage);
        StackPane.setAlignment(tagView,Pos.TOP_LEFT);
        tagStack.getChildren().add(tagView);
      }
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
