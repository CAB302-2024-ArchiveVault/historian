import com.example.historian.models.location.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocationTest {
    private Location location;

    @BeforeEach
    public void setup() {
        location = new Location("testLocation");
    }

    @Test
    public void testGetId() {
        location.setId(100);
        assertEquals(100, location.getId());
    }

    @Test
    public void testGetLocationName(){
        location.getLocationName();
        assertEquals("testLocation", location.getLocationName());
    }



}