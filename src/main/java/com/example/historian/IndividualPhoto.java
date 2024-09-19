package com.example.historian;

import com.example.historian.models.person.IPersonDAO;
import com.example.historian.models.person.Person;
import com.example.historian.models.person.SqlitePersonDAO;
import com.example.historian.models.photo.IPhotoDAO;
import com.example.historian.models.photo.Photo;
import com.example.historian.models.photo.SqlitePhotoDAO;
import com.example.historian.models.tag.ITagDAO;
import com.example.historian.models.tag.SqliteTagDAO;
import com.example.historian.models.tag.Tag;
import com.example.historian.utils.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.event.ActionEvent;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;


public class IndividualPhoto {
    @FXML
    private Pane imagePane;
    @FXML
    private Label date;
    @FXML
    private DatePicker myDatePicker;
    @FXML
    private Button locationButton;
    @FXML
    private Button tagButton;
    @FXML
    private Button finishButton;
    @FXML
    private Button backButton;
    @FXML
    public ImageView imageDisplay;
    @FXML
    public HBox editOptionsHBox;
    @FXML
    public HBox tagOptionsHBox;
    @FXML
    public TextField firstNameTextField;
    @FXML
    public TextField lastNameTextField;

    public Photo selectedPhoto;
    public static int clickedImageId;
    private IPhotoDAO photoDAO;
    private Double xCoord;
    private Double yCoord;
    private Boolean tagModeOn = false;
    private IPersonDAO personDAO;
    private ITagDAO tagDAO;
    private List<Tag> tags;

    @FXML
    public void initialize() throws IOException{
        photoDAO = new SqlitePhotoDAO();
        personDAO = new SqlitePersonDAO();
        tagDAO = new SqliteTagDAO();
        tags = new ArrayList<Tag>();
        selectedPhoto = photoDAO.getPhoto(clickedImageId);
        imageDisplay.setImage(selectedPhoto.getImage());

        if(photoDAO.getPhoto(clickedImageId).getDate() != null){
            String myFormattedDate = photoDAO.getPhoto(clickedImageId).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            date.setText(myFormattedDate);
        }
        imageDisplay.setOnMouseClicked(this::handleImageViewClick);

        // TODO: NEED TO GET ALL TAGS ASSOCIATED WITH PHOTO AND SAVE INTO 'tags'
        // TODO: THEN CALL renderAllTags()
    }

    private void handleImageViewClick(MouseEvent event) {
        if(!tagModeOn) { return; }
        removeAllCircles();
        xCoord = event.getX();
        yCoord = event.getY();

        Circle circle = new Circle(xCoord, yCoord, 5, Color.BLUE);
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

    @FXML
    protected void onBackButtonClick() throws IOException {
        StageManager.switchScene("gallery-view.fxml");
    }

    @FXML
    public void getDate(ActionEvent event) {
        LocalDate myDate = myDatePicker.getValue();
        selectedPhoto.setDate(Date.from(myDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        String myFormattedDate = myDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        date.setText(myFormattedDate);
    }

    @FXML
    public void onfinishButtonClick() throws IOException {
        photoDAO.updatePhoto(selectedPhoto);
        StageManager.switchScene("gallery-view.fxml");
    }

    private void disableButtonsTagModeOn() {
        if(tagModeOn){
            locationButton.setDisable(true);
            backButton.setDisable(true);
            tagButton.setText("Cancel");
        }else {
            locationButton.setDisable(false);
            backButton.setDisable(false);
            tagButton.setText("Tag");
        }
    }

    @FXML
    public void onTagSaveButtonClick() throws IOException {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();

        Person person = new Person(firstName, lastName);
        personDAO.addPerson(person);

        Tag tag = new Tag(clickedImageId, person, xCoord.intValue(), yCoord.intValue());
        tags.add(tag);
        tagDAO.addTag(tag);
        renderTag(tag);

        setTagOptionsVisible(false);
        removeAllCircles();
    }

    @FXML
    public void onTagCancelButtonClick() throws IOException {
        // TODO: NEED TO CANCEL ALL TAG MODIFICATIONS
        // Could do this by using a copy of the tag list from the photo object.
        // Then if save is clicked set the photo tag list to the copied one.
        // If cancel is clicked do not do anything.
        setTagOptionsVisible(false);
        removeAllCircles();
    }

    private void setTagOptionsVisible(Boolean isTrue) {
        editOptionsHBox.setVisible(!isTrue);
        editOptionsHBox.setManaged(!isTrue);
        tagOptionsHBox.setVisible(isTrue);
        tagOptionsHBox.setManaged(isTrue);
    }

    private void deleteAllRenderedTags() {
        imagePane.getChildren().removeIf(node -> node instanceof StackPane);
        imagePane.getChildren().removeIf(node -> node instanceof Polygon);
    }

    private void renderTag(Tag tag) {
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
            // TODO: THIS FUNCTION IS CALLED WHEN X BUTTON IS CLICKED
            imagePane.getChildren().removeAll(tagPane, triangle, closeButton);
            tags.remove(tag);
        });

        imagePane.getChildren().add(closeButton);

        label.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            tagPane.setLayoutX(x - newBounds.getWidth() / 2);
            tagPane.setLayoutY(y + triangleSize);

            closeButton.setLayoutX(x - newBounds.getWidth() / 2 + newBounds.getWidth() - 10);
            closeButton.setLayoutY(y);
        });
    }

    private void renderAllTags() {
        deleteAllRenderedTags();
        for (Tag tag : tags) {
            renderTag(tag);
        }
    }

    private void removeAllCircles() {
        imagePane.getChildren().removeIf(node -> node instanceof Circle);
    }

    @FXML
    public void onTagButtonClick() throws IOException {
        if (tagModeOn) {
            // TODO: THIS IS WHERE TAG MODE IS EXITED
            deleteAllRenderedTags();
        } else {
            // TODO: THIS IS WHERE TAG MODE IS ENTERED
            renderAllTags();
        }
        tagModeOn = !tagModeOn;
        disableButtonsTagModeOn();
    }
}




