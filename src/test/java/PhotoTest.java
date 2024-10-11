import com.example.historian.models.photo.Photo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class PhotoTest{
    private Photo photo;

    @BeforeEach
    protected void setup() {
        byte[] exampleImage = new byte[] { 0x1, 0x2, 0x3, 0x4 };
        photo = new Photo(exampleImage, "png", "this is a image");
    }

    @Test
    protected void testGetSetId(){
        photo.setId(15);
        assertEquals(15, photo.getId());
    }

    @Test
    protected void testGetImageAsBytes() {
        // Retrieve the image data from the photo using the method
        byte[] retrievedImage = photo.getImageAsBytes();

        assertNotNull(retrievedImage);

        byte[] expectedImage = new byte[]{0x1, 0x2, 0x3, 0x4};

        assertArrayEquals(expectedImage, retrievedImage);
    }

    @Test
    protected void testGetImageType(){
        //photo.getImageType();
        assertEquals("png",photo.getImageType());
    }

    @Test
    protected void testGetDescription(){
        //photo.getDescription();
        assertEquals("this is a image", photo.getDescription());

    }
}

