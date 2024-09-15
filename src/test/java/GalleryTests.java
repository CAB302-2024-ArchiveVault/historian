import static org.junit.jupiter.api.Assertions.*;

import com.example.historian.GalleryController;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class GalleryTests {

    private GalleryController galleryController;

    @BeforeAll
    public static void initializeToolKit() throws Exception{
        if (!Platform.isFxApplicationThread()) {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(() -> latch.countDown());
            latch.await();
        }
    }
    // needed because of javaFX components

    @BeforeEach
    public void setUp() {
        galleryController = new GalleryController();

        GalleryController.imageDatabase.clear();
        // clear database

        galleryController.backButton = Mockito.mock(Button.class);
        galleryController.forwardButton = Mockito.mock(Button.class);
        galleryController.Image1 = new ImageView();
        galleryController.Image2 = new ImageView();
        galleryController.Image3 = new ImageView();
        galleryController.Image4 = new ImageView();
        galleryController.Image5 = new ImageView();
        galleryController.Image6 = new ImageView();
        galleryController.accountText = new Text();
        // Initialize JavaFX components
    }

    @Test
    public void testAddImages() {
        List<File> filesToAdd = new ArrayList<>();
        filesToAdd.add(new File("image1.jpg"));
        filesToAdd.add(new File("image2.jpg"));

        GalleryController.imageDatabase.addAll(filesToAdd);
        assertEquals(2, GalleryController.imageDatabase.size(), "Image database should contain 2 images.");
    }

    @Test
    public void testDisplayPhotos() {
        // Add mock images to the database
        GalleryController.imageDatabase.add(new File("image1.jpg"));
        GalleryController.imageDatabase.add(new File("image2.jpg"));

        // Call the method to display photos
        galleryController.displayPhotos();

        // Verify if the images are set to the image views
        // Add assertions or verifications if needed.
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
    public void testBackButtonClick() {
        GalleryController.imageDatabase.add(new File("image1.jpg"));
        GalleryController.imageDatabase.add(new File("image2.jpg"));

        // Go forward and then back to test the navigation
        galleryController.imagepage = 1;
        galleryController.onBackButtonClick();

        assertEquals(0, galleryController.imagepage, "Image page should decrement to 0.");
    }

    @Test
    public void testForwardButtonClick() {
        for (int i = 1; i <= 10; i++) {
            GalleryController.imageDatabase.add(new File("image" + i + ".jpg"));
        }

        // Test forward button
        galleryController.imagepage = 0;
        galleryController.onForwardButtonClick();

        assertEquals(1, galleryController.imagepage, "Image page should increment to 1.");
    }
}
