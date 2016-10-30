import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.ArrayList;

public class ScraperTests {

    @Test
    public void testFallCourse() {
        Scraper scraper = new Scraper("Fall");
        ArrayList<Course> courses = scraper.getCourseList("CENG");
        Course first = courses.get(0);
        Course last = courses.get(courses.size() - 1);
        Course someMiddleElement = courses.get(778);

        Course actualFirst = new Course("AERO", 121, 1, "Kira", "Abercromby", "\u00a0", "Lec", "M", "09:10 AM", "10:00 AM", "Fall", 2016, "033-0286");
        Course actualMid = new Course("CE", 336, 1, "Misgana", "Muleta", "\u00a0", "Lec", "MW", "02:10 PM", "04:00 PM", "Fall", 2016, "034-0227");
        Course actualLast = new Course("EE", 599, 5, "Xiaozheng", "Zhang", "\u00a0", "Ind", "\u00a0", "\u00a0", "\u00a0", "Fall", 2016, "\u00a0");

        assert first.equals(actualFirst);
        assert last.equals(actualLast);
        assert someMiddleElement.equals(actualMid);
    }

    @Test
    public void testFallGEs() {
        Scraper scraper = new Scraper("Fall");
        ArrayList<Course> ges = scraper.getGEs();
        Course first = ges.get(0);
        Course last = ges.get(ges.size() - 1);

        Course coop = new Course("ARCH", 485, 1, "M", "McDonald", "COOP", "Ind", "\u00a0", "\u00a0", "\u00a0", "Fall", 2016, "\u00a0");
        Course actualLast = new Course("SPAN", 111, 2, "N", "Rucci", "USCP", "Act", "MW", "09:40 AM", "10:00 AM", "Fall", 2016, "010-0111");

        assert first.equals(coop);
        assert last.equals(actualLast);

    }

    @Test
    public void testSpring() {
        assert "TODO".equals("This hasn't been implemented");
    }
}
