import com.example.historian.models.gallery.Gallery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static com.example.historian.models.photo.MockPhotoDAO.photos;
import static org.junit.jupiter.api.Assertions.*;

public class GalleryTest {
    private Gallery gallery;

    @BeforeEach
    public void setup() {
        gallery = new Gallery("title", photos );
    }

    @Test
    public void testGetId() {
        gallery.setId(3);
        assertEquals(3, gallery.getId());
    }

    @Test
    public void testGetTitle() {
        assertEquals("title", gallery.getTitle());
    }

    @Test
    public void testGetPhotos() {
        assertNotNull(gallery.getPhotos());
        assertEquals(photos.size(), gallery.getPhotos().size());
    }

}

