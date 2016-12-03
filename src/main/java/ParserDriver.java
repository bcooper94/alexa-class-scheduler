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
    public static void main(String[] args) {
        String targetQuarter = (args.length > 0) ? args[0] : null;
        Scraper scraper = new Scraper(targetQuarter);

        ArrayList<Course> courses = scraper.getCourseList("CENG");
        ArrayList<Course> ges = scraper.getGEs();
        System.out.println("Num courses: " + courses.size());

        try {
            Scanner scan = new Scanner(new File("AWSCredentials.txt"));
            String[] keys = new String[2];

            for (int i = 0; i < 2 && scan.hasNextLine(); i++) {
                keys[i] = scan.nextLine();
            }

            AWSCredentials credentials = new BasicAWSCredentials(keys[0],
                    keys[1]);
            Database db = new Database(credentials);
            db.create(courses);
        }
        catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
        }

//        db.create(ges);

//        for(Course c : courses) {
//            System.out.println(
//                    "dept: " + c.department + " " +
//                    " courseNum: " + c.courseNumber +
//                    " sect: " + c.section +
//                    " profFirst: " + c.profFirstName +
//                    " profLast: " + c.profLastName +
//                    " req: " + c.requirement +
//                    " type: " + c.type +
//                    " days: " + c.days +
//                    " start: " + c.start +
//                    " end: " + c.end +
//                    " quarter: " + c.quarter +
//                    " year: " + c.year +
//                    " loc: " + c.location);
//        }

//        for(Course c : ges) {
//            System.out.println(c.name + " " + c.section + " \'" + c.professor + "\' " +
//              c.type + " " + c.days + " " + c.start + " " + c.end + " " + c.location);
//        }
    }
}
