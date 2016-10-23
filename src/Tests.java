import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class Tests {
  @Test
  public void createTest() {
     Database db = new Database();
     Course c = new Course();
     c.name = "Test 1";
     c.section = 1;
     db.create(c);
     db.read().forEach((rc) -> {
        assertEquals("Test 1", rc.name);
        assertEquals(1, rc.section);
     });
     db.delete(c);
  }
}
