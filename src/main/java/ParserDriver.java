import java.util.ArrayList;

public class ParserDriver {
    public static void main(String[] args) {
        String targetQuarter = (args.length > 0) ? args[0] : null;
        Scraper scraper = new Scraper(targetQuarter);

        ArrayList<Course> courses = scraper.getCourseList("CENG");
        ArrayList<Course> ges = scraper.getGEs();

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
