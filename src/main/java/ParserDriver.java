import dal.Course;
import dal.Database;

import java.util.ArrayList;

public class ParserDriver {
    private static final String[] QUARTERS = {"Fall", "Winter", "Spring"};
    private static final String[] COLLEGES = {
            "CAGR", "CAED", "OCOB", "CLA", "CENG", "CSM", "ALL"
    };

    public static void main(String[] args) {
        for (String quarter : QUARTERS) {
            Scraper scraper = new Scraper(quarter);

            Database db = new Database();
            ArrayList<Course> courses;
            for (String college : COLLEGES) {
                courses = scraper.getCourseList(college);
                db.create(courses);
            }

            ArrayList<Course> ges = scraper.getGEs();
            db.create(ges);
        }
    }
}
