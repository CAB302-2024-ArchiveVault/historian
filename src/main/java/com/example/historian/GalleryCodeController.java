package com.example.historian;

import com.example.historian.auth.AuthSingleton;
import com.example.historian.models.gallery.Gallery;
import com.example.historian.models.gallery.IGalleryDAO;
import com.example.historian.models.gallery.SqliteGalleryDAO;
import com.example.historian.models.location.ILocationDAO;
import com.example.historian.models.location.Location;
import com.example.historian.models.person.IPersonDAO;
import com.example.historian.models.person.Person;
import com.example.historian.models.photo.IPhotoDAO;
import com.example.historian.models.photo.Photo;
import com.example.historian.models.photo.SqlitePhotoDAO;
import com.example.historian.utils.GallerySingleton;
import com.example.historian.utils.StageManager;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.historian.utils.StageManager.switchScene;

public class GalleryCodeController {
    @FXML public GridPane imageContainer;
    @FXML public Button exitButton;
    @FXML public Button forwardButton;
    @FXML public Button backButton;

    private int photosPerPage = 12;
    private int photosPerRow = 4;

    private AuthSingleton authSingleton;
    private IPhotoDAO photoDAO;
    private GallerySingleton gallerySingleton;
    private IGalleryDAO galleryDAO;
    private Gallery gallery;
    public List<Photo> photoList;
    private String galleryCode;

    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    Image tagImage = new Image("file:src/icons/tag.png");

    private ILocationDAO locationDAO;
    private ObservableList<Location> locationList;
    private IPersonDAO personDAO;
    private ObservableList<Person> personList;


    @FXML
    public void initialize() throws IOException {
        authSingleton = AuthSingleton.getInstance();
        if (!authSingleton.checkGalleryCode()) {
            StageManager.switchToHomepage();
        }

        galleryCode = authSingleton.getGalleryCode();

        photoDAO = new SqlitePhotoDAO();
        gallerySingleton = GallerySingleton.getInstance();

        galleryDAO = new SqliteGalleryDAO();
        gallery = galleryDAO.getGalleryByKey(galleryCode);
        photoList = gallery.getPhotos();
        displayPhotos();
        buttonUpdate();
    }

    @FXML
    protected void onExitButtonClick() throws IOException {
        authSingleton.setGalleryCodeNull();
        StageManager.switchToHomepage();

    }


    private void checkToDisplayIndividualPhoto() {
        if (!gallerySingleton.isPhotoQueueEmpty()) {
            try {
                switchScene("individualPhoto-view.fxml", 580, photoDAO.getPhoto(gallerySingleton.firstPhotoInQueueID()).getAdjustedImageHeight() + 280);
                //StageManager.switchScene("individualPhoto-view.fxml");
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

            //Create the VBox
            VBox vbox = new VBox();

            // Create the imageview
            ImageView imageView = new ImageView();
            imageView.setPreserveRatio(true);
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
            GridPane.setHalignment(vbox, HPos.CENTER );
            GridPane.setValignment(vbox, VPos.BOTTOM);
            vbox.getChildren().add(tagStack);
            vbox.setAlignment(Pos.CENTER);
            tagStack.getChildren().add(imageView);
            StackPane.setAlignment(imageView,Pos.BOTTOM_CENTER);
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
