
import calPolyScheduler.intents.CourseResponseBuilder;
import dal.Course;
import dal.QueryKey;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CourseResponseBuilderTest {

    @Test
    public void testThis() {
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(new Course("department", "courseNumber", "section",
                "profFirstName", "profLastName", "SAD", "Ind", "MTWRF",
                "8:00 PM", "2:00 AM", "Fall", "2016", "022-0434"));
        List<QueryKey> types =  Arrays.asList(
                QueryKey.DEPARTMENT,
                QueryKey.COURSE_NUM,
                QueryKey.SECTION,
                QueryKey.FIRST_NAME,
                QueryKey.LAST_NAME,
                QueryKey.REQUIREMENT,
                QueryKey.TYPE,
                QueryKey.DAYS,
                QueryKey.START,
                QueryKey.END,
                QueryKey.QUARTER,
                QueryKey.YEAR,
                QueryKey.LOCATION,
                QueryKey.COLLEGE);

        CourseResponseBuilder crb = new CourseResponseBuilder();
        assertEquals("<say-as interpret-as=\"spell-out\">DEPARTMENT</say-as> " +
            "courseNumber " + "SECTION " + "PROFFIRSTNAME " + "PROFLASTNAME " +
                "<say-as interpret-as=\"spell-out\">SAD</say-as> " + "independent study " +
                "Monday Tuesday Wednesday Thursday Friday " +
                "<say-as interpret-as=\"time\">8:00 PM</say-as> " +
                "<say-as interpret-as=\"time\">2:00 AM</say-as> " + "FALL " + "2016 " +
                "building 22 room 434 " + "No college for you ,",
                crb.convertCourse(courses, types));
    }
}
