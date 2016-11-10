import static org.junit.Assert.assertEquals;

import dal.*;
import org.junit.Test;

import java.util.Arrays;
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
     List<Query> queryList = new ArrayList<Query>();
     queryList.add(new Query(QueryKey.DEPARTMENT, "CSC", QueryOperation.EQUAL, null, null));
     List<Course> resultList = db.read(queryList);
     assertEquals(5, resultList.size());
     db.read(queryList).forEach( (rc) -> {
        assertEquals("CSC", rc.department);
        assertEquals("101", rc.courseNumber);
     });
     assertEquals(1,db.read(Arrays.asList(
        new Query(QueryKey.DEPARTMENT, "CSC", QueryOperation.EQUAL, QueryLogic.AND, null),
        new Query(QueryKey.SECTION, "1", QueryOperation.GREATER_THAN, QueryLogic.AND, Query.OPEN),
        new Query(QueryKey.SECTION, "3", QueryOperation.LESS_THAN, null, Query.CLOSE)
     )).size());
  }
}
