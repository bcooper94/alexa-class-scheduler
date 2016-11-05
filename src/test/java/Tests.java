import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

public class Tests {
  /*@Test
  public void createOneTest() {
     Database db = new Database();
     Course c = new Course();
     c.department = "CSC";
     c.courseNumber = "101";
     c.section = "1";
     db.create(c);
     db.read().forEach((rc) -> {
        assertEquals("CSC", rc.department);
        assertEquals("101", rc.courseNumber);
        assertEquals("1", rc.section);
     });
     //db.delete(c);
  }*/

  @Test
  public void createManyTest() {
     Database db = new Database();
     List<Course> courses = new ArrayList<Course>();
     for (int i = 0; i < 5; i++) {
        Course c = new Course();
        c.department = "CSC";
        c.courseNumber = "101";
        c.section = Integer.toString(i);
        courses.add(c);
     } 
     db.create(courses);
     int[] section = {0};
     db.read().forEach( (rc) -> {
        assertEquals("CSC", rc.department);
        assertEquals("101", rc.courseNumber);
        assertEquals(Integer.toString(section[0]), rc.section);
        section[0]++;
     });
  }
}
