import static org.junit.jupiter.api.Assertions.*;

import com.example.historian.GalleryController;
import com.example.historian.models.photo.Photo;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class GalleryTests {

    private GalleryController galleryController;

    @BeforeAll
    public static void initializeToolKit() throws Exception {
        if (!Platform.isFxApplicationThread()) {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(() -> latch.countDown());
            latch.await();
        }
    }

    @BeforeEach
    public void setUp() {
        galleryController = new GalleryController();

        galleryController.backButton = Mockito.mock(Button.class);
        galleryController.forwardButton = Mockito.mock(Button.class);
        galleryController.Image1 = new ImageView();
        galleryController.Image2 = new ImageView();
        galleryController.Image3 = new ImageView();
        galleryController.Image4 = new ImageView();
        galleryController.Image5 = new ImageView();
        galleryController.Image6 = new ImageView();
        galleryController.accountText = new Text();

        // Initialize photoList to avoid null pointer exceptions during tests
        galleryController.photoList = new ArrayList<>();
    }

    @Test
    public void testAddImages() throws Exception {
        // Convert image files to byte arrays
        byte[] imageData1 = Files.readAllBytes(new File("image1.jpg").toPath());
        byte[] imageData2 = Files.readAllBytes(new File("image2.jpg").toPath());

        // Create Photo objects with the required byte[], description, and filename
        Photo photo1 = new Photo(imageData1, "Description for image1", "image1.jpg");
        Photo photo2 = new Photo(imageData2, "Description for image2", "image2.jpg");

        // Add them to the photoList
        galleryController.photoList.add(photo1);
        galleryController.photoList.add(photo2);

        // Validate that the photos are added
        assertEquals(2, galleryController.photoList.size(), "Photo list should contain 2 images.");
    }

    @Test
    public void testDisplayPhotos() throws Exception {
        // Convert image files to byte arrays
        byte[] imageData1 = Files.readAllBytes(new File("image1.jpg").toPath());
        byte[] imageData2 = Files.readAllBytes(new File("image2.jpg").toPath());

        // Add mock images to the photoList
        galleryController.photoList.add(new Photo(imageData1, "Description for image1", "image1.jpg"));
        galleryController.photoList.add(new Photo(imageData2, "Description for image2", "image2.jpg"));

        // Call the method to display photos
        galleryController.displayPhotos();

        // Verify if the images are set to the image views
        assertNotNull(galleryController.Image1.getImage(), "Image1 should be set.");
        assertNotNull(galleryController.Image2.getImage(), "Image2 should be set.");
    }

    //    @Test
//    public void testNavigationButtons() {
//        // Add more than 6 images to enable navigation
//        for (int i = 1; i <= 10; i++) {
//            GalleryController.imageDatabase.add(new File("image" + i + ".jpg"));
//        }
//
//        // Call buttonUpdate method
//        galleryController.buttonUpdate();
//
//        // Use assertions to check the visibility
//        assertFalse(galleryController.backButton.isVisible(), "Back button should be hidden initially.");
//        assertTrue(galleryController.forwardButton.isVisible(), "Forward button should be visible when there are more than 6 images.");
//    }
    // The mockito Button.class may doesnt seem to emulating the actual class
    // Fix later or scrap

    @Test
    public void testBackButtonClick() throws Exception {
        byte[] imageData1 = Files.readAllBytes(new File("image1.jpg").toPath());
        byte[] imageData2 = Files.readAllBytes(new File("image2.jpg").toPath());

        galleryController.photoList.add(new Photo(imageData1, "Description for image1", "image1.jpg"));
        galleryController.photoList.add(new Photo(imageData2, "Description for image2", "image2.jpg"));

        // Go forward and then back to test the navigation
        galleryController.photoPage = 1;
        galleryController.onBackButtonClick();

        assertEquals(0, galleryController.photoPage, "Photo page should decrement to 0.");
    }

    @Test
    public void testForwardButtonClick() throws Exception {
        // Add mock images to the photoList
        for (int i = 1; i <= 10; i++) {
            byte[] imageData = Files.readAllBytes(new File("image" + i + ".jpg").toPath());
            galleryController.photoList.add(new Photo(imageData, "Description for image" + i, "image" + i + ".jpg"));
        }

        // Test forward button
        galleryController.photoPage = 0;
        galleryController.onForwardButtonClick();

        assertEquals(1, galleryController.photoPage, "Photo page should increment to 1.");
    }
}
