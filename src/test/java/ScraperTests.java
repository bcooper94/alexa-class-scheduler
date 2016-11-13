import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.ArrayList;

public class ScraperTests {

    @Test
    public void testFallCourse() {
        Scraper scraper = new Scraper("Fall");
        ArrayList<dal.Course> courses = scraper.getCourseList("CENG");
        dal.Course first = courses.get(0);
        dal.Course last = courses.get(courses.size() - 1);
        dal.Course someMiddleElement = courses.get(778);

        dal.Course actualFirst = new dal.Course("AERO", "121", "1", "KIRA", "ABERCROMBY", "\u00a0", "LEC", "M", "09:10 AM", "10:00 AM", "FALL", "2016", "033-0286");
        dal.Course actualMid = new dal.Course("CE", "336", "1", "MISGANA", "MULETA", "\u00a0", "LEC", "MW", "02:10 PM", "04:00 PM", "FALL", "2016", "034-0227");
        dal.Course actualLast = new dal.Course("EE", "599", "5", "XIAOZHENG", "ZHANG", "\u00a0", "IND", "\u00a0", "\u00a0", "\u00a0", "FALL", "2016", "\u00a0");

        assert first.equals(actualFirst);
        assert last.equals(actualLast);
        assert someMiddleElement.equals(actualMid);
    }

    @Test
    public void testFallGEs() {
        Scraper scraper = new Scraper("Fall");
        ArrayList<dal.Course> ges = scraper.getGEs();
        dal.Course first = ges.get(0);
        dal.Course last = ges.get(ges.size() - 1);

        dal.Course coop = new dal.Course("ARCH", "485", "1", "M", "MCDONALD", "COOP", "IND", "\u00a0", "\u00a0", "\u00a0", "FALL", "2016", "\u00a0");
        dal.Course actualLast = new dal.Course("SPAN", "111", "2", "N", "RUCCI", "USCP", "ACT", "MW", "09:40 AM", "10:00 AM", "FALL", "2016", "010-0111");

        assert first.equals(coop);
        assert last.equals(actualLast);

    }

    @Test
    public void testSpring() {
        Scraper scraper = new Scraper("Spring");
        ArrayList<dal.Course> courses = scraper.getCourseList("CSM");
        dal.Course first = courses.get(0);
        dal.Course last = courses.get(courses.size() - 1);

        dal.Course actualFirst = new dal.Course("BIO", "400", "2", "NIKKI", "ADAMS", "\u00a0", "IND", "\u00a0", "\u00a0", "\u00a0", "SPRING", "2017", "\u00a0");
        dal.Course actualLast = new dal.Course("CHEM", "331", "3", "MATTHEW", "ZOERB", "\u00a0", "LAB", "TR", "12:10 PM", "03:00 PM", "SPRING", "2017", "180-0376");

        assert first.equals(actualFirst);
        assert last.equals(actualLast);
    }
}
