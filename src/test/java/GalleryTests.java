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

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class GalleryTests {

    private GalleryController galleryController;

    @BeforeAll
    public static void initializeToolKit() throws Exception {
        // activate headless mode to avoid running tests with a graphical interface
        System.setProperty("java.awt.headless", "true");

        if (!Platform.isFxApplicationThread()) {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(latch::countDown);
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
    public void testAddImages() {
        // Create mock byte arrays to simulate image data
        byte[] imageData1 = new byte[]{0, 1, 2};
        byte[] imageData2 = new byte[]{3, 4, 5};

        Photo photo1 = new Photo(imageData1, "Description for image1", "image1.jpg");
        Photo photo2 = new Photo(imageData2, "Description for image2", "image2.jpg");

        galleryController.photoList.add(photo1);
        galleryController.photoList.add(photo2);

        // Validate that the photos are added
        assertEquals(2, galleryController.photoList.size(), "Photo list should contain 2 images.");
    }

    @Test
    public void testDisplayPhotos() {
        byte[] imageData1 = new byte[]{0, 1, 2};
        byte[] imageData2 = new byte[]{3, 4, 5};

        // Add mock images to the photoList
        galleryController.photoList.add(new Photo(imageData1, "Description for image1", "image1.jpg"));
        galleryController.photoList.add(new Photo(imageData2, "Description for image2", "image2.jpg"));

        // Call the method to display photos
        galleryController.displayPhotos();

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
    // The mockito Button.class does not seem to emulating the actual class
    // Fix later or scrap

    @Test
    public void testBackButtonClick() {
        byte[] imageData1 = new byte[]{0, 1, 2};
        byte[] imageData2 = new byte[]{3, 4, 5};

        galleryController.photoList.add(new Photo(imageData1, "Description for image1", "image1.jpg"));
        galleryController.photoList.add(new Photo(imageData2, "Description for image2", "image2.jpg"));

        // Go forward and then back to test the navigation
        galleryController.photoPage = 1;
        galleryController.onBackButtonClick();

        assertEquals(0, galleryController.photoPage, "Photo page should decrement to 0.");
    }

    @Test
    public void testForwardButtonClick() {
        // Create mock byte arrays to simulate image data
        for (int i = 1; i <= 10; i++) {
            byte[] imageData = new byte[]{(byte) i};
            galleryController.photoList.add(new Photo(imageData, "Description for image" + i, "image" + i + ".jpg"));
        }

        // Test forward button
        galleryController.photoPage = 0;
        galleryController.onForwardButtonClick();

        assertEquals(1, galleryController.photoPage, "Photo page should increment to 1.");
    }
}
