import static org.junit.Assert.assertEquals;

import dal.Course;
import dal.Database;
import dal.Query;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

public class Tests {
  /*@Test
  public void createOneTest() {
     dal.Database db = new dal.Database();
     dal.Course c = new dal.Course();
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
     List<Query> queryList = new ArrayList<Query>();
     queryList.add(new Query("department", "CSC", Query.EQUAL, null, null));
     List<Course> resultList = db.read(queryList);
     assertEquals(5, resultList.size());
     db.read(queryList).forEach( (rc) -> {
        assertEquals("CSC", rc.department);
        assertEquals("101", rc.courseNumber);
     });
     /*assertEquals(1,db.read(Arrays.asList(
        new dal.Query("department", "CSC", dal.Query.EQUAL, dal.Query.AND, null),
        new dal.Query("section", "1", dal.Query.GREATER_THAN, dal.Query.OR, dal.Query.OPEN),
        new dal.Query("section", "3", dal.Query.LESS_THAN, null, dal.Query.CLOSE)
     )).size());*/
  }
}
