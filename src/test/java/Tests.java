import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class Tests {
  @Test
  public void createTest() {
     Database db = new Database();
     Course c = new Course();
     c.department = "CSC";
     c.courseNumber = 101;
     c.section = 1;
     /*db.create(c);
     db.read().forEach((rc) -> {
        assertEquals("CSC", rc.department);
        assertEquals(101, rc.courseNumber);
        assertEquals(1, rc.section);
     });
     db.delete(c);*/
  }
}
