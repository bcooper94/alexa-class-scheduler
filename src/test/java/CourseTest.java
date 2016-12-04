import dal.Course;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by brandon on 12/3/16.
 */
public class CourseTest {
    @Test
    public void testCourse() {
        Course c1 = new Course("CPE", "101", "7", "Bob", "Smith", "", "LEC", "MWF", "07:30 AM", "09:30 AM", "FALL", "2016", "52"),
                c2 = new Course("ME", "315", "02", "Bob", "Smith", "", "LEC", "MW", "07:00 PM", "09:00 PM", "WINTER", "2016", "52");
        assertEquals("07:30", c1.start);
        assertEquals("09:30", c1.end);
        assertEquals("19:00", c2.start);
        assertEquals("21:00", c2.end);
    }
}
