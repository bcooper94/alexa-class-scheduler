import calPolyScheduler.Schedule;
import dal.Course;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by brandon on 12/3/16.
 */
public class ScheduleTest {
    @Test
    public void testSchedule() {
        Course c1 = new Course("CPE", "101", "7", "Bob", "Smith", "", "LEC", "MWF", "07:30 AM", "09:30 AM", "FALL", "2016", "52"),
                c2 = new Course("CPE", "560", "3", "George", "Smith", "", "LAB", "MW", "07:00 AM", "09:30 AM", "FALL", "2016", "52"),
                c3 = new Course("IME", "152", "01", "Bob", "Smith", "", "LEC", "TR", "07:00 AM", "09:30 AM", "FALL", "2016", "52"),
                c4 = new Course("ME", "315", "02", "Bob", "Smith", "", "LEC", "MTRF", "09:00 AM", "11:00 AM", "FALL", "2016", "52"),
                c5 = new Course("ME", "315", "02", "Bob", "Smith", "", "LEC", "MW", "07:00 PM", "09:00 PM", "WINTER", "2016", "52"),
                c6 = new Course("ME", "315", "02", "Bob", "Smith", "", "LEC", "MTRF", "07:00 PM", "09:00 PM", "FALL", "2016", "52");
        Schedule schedule = new Schedule();
        assertTrue(schedule.addCourse(c1));
        assertFalse(schedule.addCourse(c2));
        assertTrue(schedule.addCourse(c3));
        assertFalse(schedule.addCourse(c4));
        assertFalse(schedule.addCourse(c5));
        assertTrue(schedule.addCourse(c6));
    }
}
