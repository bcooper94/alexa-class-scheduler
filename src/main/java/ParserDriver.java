import java.util.ArrayList;

public class ParserDriver {
    public static void main(String[] args) {
        String targetQuarter = (args.length > 0) ? args[0] : null;
        Scraper scraper = new Scraper(targetQuarter);

        ArrayList<Course> courses = scraper.getCourseList("CENG");
        ArrayList<Course> ges = scraper.getGEs();

//        for(Course c : courses) {
//            System.out.println(c.name + " " + c.section + " \'" + c.professor + "\' " +
//              c.type + " " + c.days + " " + c.start + " " + c.end + " " + c.location);
//        }

//        for(Course c : ges) {
//            System.out.println(c.name + " " + c.section + " \'" + c.professor + "\' " +
//              c.type + " " + c.days + " " + c.start + " " + c.end + " " + c.location);
//        }
    }
}
