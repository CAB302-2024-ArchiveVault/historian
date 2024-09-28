import com.example.historian.models.person.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonTest {
    private Person person;

    @BeforeEach
    public void setup() {
        person = new Person("testFirstname", "testLastName");
    }

    @Test
    public void testGetId() {
        person.setId(5);
        assertEquals(5, person.getId());
    }

    @Test
    public void testGetFirstName(){
        person.getFirstName();
        assertEquals("testFirstname", person.getFirstName());
    }

    @Test
    public void testGetLastName(){
        person.getLastName();
        assertEquals("testLastName", person.getLastName());
    }

    @Test
    public void testFullName(){
        person.getFullName();
        assertEquals("testFirstname testLastName", person.getFullName());
    }
}

