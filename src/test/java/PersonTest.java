import com.example.historian.models.person.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonTest {
    private Person person;

    @BeforeEach
    protected void setup() {
        person = new Person("testFirstName", "testLastName");
    }

    @Test
    protected void testGetId() {
        person.setId(5);
        assertEquals(5, person.getId());
    }

    @Test
    protected void testGetFirstName(){
        //person.getFirstName();
        assertEquals("testFirstName", person.getFirstName());
    }

    @Test
    protected void testGetLastName(){
        //person.getLastName();
        assertEquals("testLastName", person.getLastName());
    }

    @Test
    protected void testFullName(){
        //person.getFullName();
        assertEquals("testFirstName testLastName", person.getFullName());
    }
}

