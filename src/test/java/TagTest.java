import com.example.historian.models.person.Person;
import com.example.historian.models.tag.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TagTest {
    private Tag tag;

    @BeforeEach
    protected void setup() {
        tag = new Tag(4, new Person("testFirstName", "testLastName"), 50, 72);
    }

    @Test
    protected void testGetId() {
        tag.setId(100);
        assertEquals(100, tag.getId());
    }

    @Test
    protected void testGetPhotoId (){
        //tag.getPhotoId();
        assertEquals(4, tag.getPhotoId());
    }

    @Test
    protected void testGetCoordinates(){
        //tag.getCoordinates();
        assertArrayEquals(new int[]{50, 72}, tag.getCoordinates());
    }

    @Test
    protected void testGetPerson(){
        assertEquals("testFirstName", tag.getPerson().getFirstName());
        assertEquals("testLastName", tag.getPerson().getLastName());
    }
}
