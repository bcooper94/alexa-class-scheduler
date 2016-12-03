import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import dal.Course;
import dal.Database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class ParserDriver {
    private static final String[] QUARTERS = {"Fall", "Winter", "Spring"};
    private static final String[] COLLEGES = {
            "CAGR", "CAED", "OCOB", "CLA", "CENG", "CSM", "ALL"
    };

    public static void main(String[] args) {
        for (String quarter : QUARTERS) {
            Scraper scraper = new Scraper(quarter);

            try {
                Scanner scan = new Scanner(new File("AWSCredentials.txt"));
                String[] keys = new String[2];

                for (int i = 0; i < 2 && scan.hasNextLine(); i++) {
                    keys[i] = scan.nextLine();
                }

                AWSCredentials credentials = new BasicAWSCredentials(keys[0],
                        keys[1]);
                Database db = new Database(credentials);
                ArrayList<Course> courses;
                for (String college : COLLEGES) {
                    courses = scraper.getCourseList(college);
//                    db.create(courses);
                    System.out.println(String.format("Adding %s courses from %s", courses.size(), college));
                }

                ArrayList<Course> ges = scraper.getGEs();
                System.out.println(String.format("Adding %s GEs", ges.size()));
//                db.create(ges);
            } catch (FileNotFoundException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
}
