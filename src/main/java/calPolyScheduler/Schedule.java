package calPolyScheduler;

import calPolyScheduler.intents.CourseResponseBuilder;
import dal.Course;
import dal.Query;
import dal.QueryKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Schedule {
    private static final SimpleDateFormat PARSE_FORMAT = new SimpleDateFormat("hh:mm a");
    private ArrayList<Course> list;
    private String quarter = "";
    private String year = "";

    public Schedule() {
        list = new ArrayList<>();
    }

    public boolean addCourse(Course newCourse) {
        if (checkConflicts(newCourse)) {
            return false;
        }
        if (quarter.equals("")) {
            quarter = newCourse.quarter;
        }
        if (year.equals("")) {
            year = newCourse.year;
        }

        list.add(newCourse);
        return true;
    }

    public boolean removeCourse(Course course) {
        for (Course c : list) {
            if (course.department.equals(c.department) && course.courseNumber.equals(c.courseNumber)
                    && course.section.equals(c.section)) {
                list.remove(c);
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public String getList() {
        CourseResponseBuilder crb = new CourseResponseBuilder();
        List<QueryKey> keys = Arrays.asList(QueryKey.DEPARTMENT, QueryKey.COURSE_NUM, QueryKey.SECTION,
                QueryKey.DAYS, QueryKey.START, QueryKey.END);
        return crb.convertCourse(list, keys);
    }

    public String getLisForList() {
        CourseResponseBuilder crb = new CourseResponseBuilder();
        List<QueryKey> keys = Arrays.asList(QueryKey.DEPARTMENT, QueryKey.COURSE_NUM, QueryKey.SECTION,
                QueryKey.DAYS, QueryKey.START, QueryKey.END);
        return crb.getCardContent(list, keys);
    }

    private boolean checkConflicts(Course newCourse) {
        String days = newCourse.days;
        for (Course c : list) {
            //check quarter and year
            if (!quarter.equals("") && !quarter.equals(newCourse.quarter) || !year.equals("") && !year.equals(newCourse.year))
                return true;

            //check class info
            if (c.department.equals(newCourse.department) && c.courseNumber.equals(newCourse.courseNumber) &&
                    c.type.equals(newCourse.type)) {
                return true;
            }

            //check time
            if (matchingDays(c.days, days) && !noConflicts(newCourse.start, newCourse.end, c.start, c.end)) {
                return true;
            }
        }
        return false;
    }

    private boolean matchingDays(String days1, String days2) {
        for (char c : days1.toCharArray()) {
            if (days2.indexOf(c) > 0) {
                return true;
            }
        }
        return false;
    }

    private boolean noConflicts(String start1, String end1, String start2, String end2) {
        try {
            Date s1 = convertToMilitaryTime(start1);
            Date e1 = convertToMilitaryTime(end1);
            Date s2 = convertToMilitaryTime(start2);
            Date e2 = convertToMilitaryTime(end2);
            return (s1.before(e2) && e1.before(s2)) ||
                    (s2.before(e1) && e2.before(s1));
        }
        catch (ParseException p) {
            System.out.println("Error parsing time " + p.getMessage());
        }
        return false;
    }

    private Date convertToMilitaryTime(String time) throws ParseException {
        return PARSE_FORMAT.parse(time);
    }
}
