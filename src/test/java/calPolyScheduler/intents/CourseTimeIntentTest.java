package calPolyScheduler.intents;

import dal.Course;
import org.junit.Test;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by tbleisch on 12/3/16.
 */
public class CourseTimeIntentTest {
    SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
    SimpleDateFormat militaryFormatter = new SimpleDateFormat("HH:mm");

    @Test
    public void courseTimeFallsInRange() throws Exception {
        String timeComparison = "before";
        String singleTime = "16:00";

        Course course = new dal.Course("AERO", "121", "1", "KIRA", "ABERCROMBY", "\u00a0", "LEC", "M", "09:10 AM", "10:00 AM", "FALL", "2016", "033-0286");
        Date courseStartTimeDate =  formatter.parse(course.start, new ParsePosition(0));
        Date courseEndTimeDate = formatter.parse(course.end, new ParsePosition(0));
        Date singleTimeDate = militaryFormatter.parse(singleTime, new ParsePosition(0));

        System.out.println(courseStartTimeDate);
        System.out.println(courseEndTimeDate);
        System.out.println(singleTimeDate);
        System.out.println(courseEndTimeDate.before(singleTimeDate));
        assertTrue(timeComparison.equals("before") && courseEndTimeDate.before(singleTimeDate));

        singleTime = "10:00";
        singleTimeDate = militaryFormatter.parse(singleTime, new ParsePosition(0));
        assertFalse(timeComparison.equals("before") && courseStartTimeDate.after(singleTimeDate));
    }

    @Test
    public void courseTimeFallsInRange1() throws Exception {
        Course course = new dal.Course("CE", "336", "1", "MISGANA", "MULETA", "\u00a0", "LEC", "MW", "02:10 PM", "04:00 PM", "FALL", "2016", "034-0227");
        Date courseStartTimeDate =  formatter.parse(course.start, new ParsePosition(0));
        Date courseEndTimeDate = formatter.parse(course.end, new ParsePosition(0));
        Date startTimeDate = militaryFormatter.parse("12:00", new ParsePosition(0));
        Date endTimeDate = militaryFormatter.parse("16:30", new ParsePosition(0));

        assertTrue((courseStartTimeDate.equals(startTimeDate) || courseStartTimeDate.after(startTimeDate))
                && (courseEndTimeDate.equals(endTimeDate) || courseEndTimeDate.before(endTimeDate)));

        startTimeDate = militaryFormatter.parse("15:00", new ParsePosition(0));
        endTimeDate = militaryFormatter.parse("16:30", new ParsePosition(0));

        assertFalse((courseStartTimeDate.equals(startTimeDate) || courseStartTimeDate.after(startTimeDate))
                && (courseEndTimeDate.equals(endTimeDate) || courseEndTimeDate.before(endTimeDate)));
    }

}