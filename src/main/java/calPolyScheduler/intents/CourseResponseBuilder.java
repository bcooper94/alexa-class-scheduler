package calPolyScheduler.intents;

import dal.Course;
import dal.QueryKey;

import java.util.List;

public class CourseResponseBuilder {
    public String convertCourse(List<Course> list, List<QueryKey> keys) {
        StringBuilder response = new StringBuilder();

        for (Course c : list) {
            for (QueryKey k : keys) {
                response.append(courseVar(c, k) + " ");
            }
            response.append(",");
        }

        return response.toString();
    }

    private String courseVar(Course c, QueryKey key) {
        switch (key) {
            case DEPARTMENT:
                return "<say-as interpret-as=\"spell-out\">" + c.department + "</say-as>";
            case COURSE_NUM:
                return c.courseNumber;
            case SECTION:
                return c.section;
            case FIRST_NAME:
                return c.profFirstName;
            case LAST_NAME:
                return c.profLastName;
            case REQUIREMENT:
                return "<say-as interpret-as=\"spell-out\">" + c.requirement + "</say-as>";
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
                return "from <say-as interpret-as=\"time\">" + c.start + "</say-as> to " +
                        "<say-as interpret-as=\"time\">" + c.end + "</say-as>";
            case START:
                return "<say-as interpret-as=\"time\">" + c.start + "</say-as>";
            case END:
                return "<say-as interpret-as=\"time\">" + c.end + "</say-as>";
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