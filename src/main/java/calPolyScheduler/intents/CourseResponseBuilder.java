package calPolyScheduler.intents;

import dal.Course;
import dal.QueryKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseResponseBuilder {
    public String convertCourse(List<Course> list, List<QueryKey> keys) {
        StringBuilder response = new StringBuilder();

        for (Course c : list) {
            for (QueryKey k : keys) {
                response.append(courseVar(c, k, false) + " ");
            }
            response.append(",");
        }

        return response.toString();
    }

    public String getCardContent(List<Course> list, List<QueryKey> keys) {
        StringBuilder response = new StringBuilder();

        for (Course c : list) {
            for (QueryKey k : keys) {
                response.append(courseVar(c, k, true) + "\n");
            }
            response.append(",");
        }

        return response.toString();
    }

    public List<String> convertCourseToList(List<Course> list, List<QueryKey> keys) {
        ArrayList<String> response = new ArrayList<>();

        for (Course c : list)
            for (QueryKey k : keys)
                response.add(courseVar(c, k, false));

        return response;
    }

    private String courseVar(Course c, QueryKey key, boolean isCard) {
        switch (key) {
            case DEPARTMENT:
                return isCard ? c.department : "<say-as interpret-as=\"spell-out\">" + c.department + "</say-as>";
            case COURSE_NUM:
                return c.courseNumber;
            case SECTION:
                return c.section;
            case FIRST_NAME:
                return c.profFirstName;
            case LAST_NAME:
                return c.profLastName;
            case REQUIREMENT:
                return isCard ? c.requirement : "<say-as interpret-as=\"spell-out\">" + c.requirement + "</say-as>";
            case TYPE:
                String courseType = "lecture";
                if (c.type.equals("ACT")) {
                    courseType = "activity";
                }
                else if (c.type.equals("IND")) {
                    courseType = "independent study";
                }
                else if (c.type.equals("LAB")){
                    courseType = "lab";
                }
                return courseType;
            case DAYS:
                return convertDays(c.days);
            case TIME_RANGE:
                return isCard ? c.start + "-" + c.end :
                        "from <say-as interpret-as=\"time\">" + c.start + "</say-as> to " +
                        "<say-as interpret-as=\"time\">" + c.end + "</say-as>";
            case START:
                return isCard ? c.start : "<say-as interpret-as=\"time\">" + c.start + "</say-as>";
            case END:
                return isCard ? c.end : "<say-as interpret-as=\"time\">" + c.end + "</say-as>";
            case QUARTER:
                return c.quarter;
            case YEAR:
                return c.year;
            case LOCATION:
                return convertLoc(c.location);
            case COLLEGE:
                return "No college for you";
        }
        return "Well somehow you done goofed";
    }

    private String convertDays(String dayAbbr) {
        StringBuilder builderTheSecond = new StringBuilder();
        for (char c : dayAbbr.toCharArray()) {
            switch(c) {
                case 'M':
                    builderTheSecond.append("Monday ");
                    break;
                case 'T':
                    builderTheSecond.append("Tuesday ");
                    break;
                case 'W':
                    builderTheSecond.append("Wednesday ");
                    break;
                case 'R':
                    builderTheSecond.append("Thursday ");
                    break;
                case 'F':
                    builderTheSecond.append("Friday ");
                    break;
            }
        }
        return builderTheSecond.toString().trim();
    }

    private String convertLoc(String loc) {
        StringBuilder builderTheThird = new StringBuilder();
        String[] loc2 = loc.split("-");

        try {
            builderTheThird.append("building " + Integer.parseInt(loc2[0]));
            builderTheThird.append(" room " + Integer.parseInt(loc2[1]));
        }
        catch (NumberFormatException nf) {
            return "some magical place in Cal Poly frowny face";
        }

        return builderTheThird.toString();
    }

    public String addSpellOut(String input) {
        return "<say-as interpret-as=\"spell-out\">" + input + "</say-as>";
    }
}
