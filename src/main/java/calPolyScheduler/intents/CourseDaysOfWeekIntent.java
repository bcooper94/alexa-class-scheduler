package calPolyScheduler.intents;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import dal.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CourseDaysOfWeekIntent extends SchedulerIntent {
    public CourseDaysOfWeekIntent(Intent intent, Session session) {
        super(intent, session);
    }

    @Override
    public SpeechletResponse createResponse() {
        log.info("Course Days of Week Intent");
        String responseText = determineUtterance();
        return setAnswer(responseText, "Course Days of Week Intent", responseText);
    }

    private String determineUtterance() {
        CourseResponseBuilder crb = new CourseResponseBuilder();
        List<Query> queryList = new ArrayList<Query>();
        String response = "";

        if (slots.get("DayOne") != null && slots.get("Department") != null) {
//            val = "You asked for what courses were offered 1 day a week";
            String[] dayKeys = new String[] {"DayOne", "DayTwo", "DayThree", "DayFour"};
            String days = "";

            for (String s : dayKeys) {
                if (slots.get(s) != null) {
                    days += slots.get(s) + " ";
                }
            }
            response = addQuarterYear(queryList);
            queryList.add(new Query(QueryKey.DEPARTMENT, slots.get("Department"), QueryOperation.EQUAL, QueryLogic.AND));
            queryList.add(new Query(QueryKey.DAYS, convertDays(days), QueryOperation.EQUAL));

            List<Course> resultList = db.read(queryList);
            List<QueryKey> types = Arrays.asList(QueryKey.DEPARTMENT, QueryKey.COURSE_NUM, QueryKey.SECTION);
            response += "Courses offered on " + days + "are " + crb.convertCourse(resultList, types);
        }
        else if (slots.get("Department") != null && slots.get("CourseNum") != null) {
            response = addQuarterYear(queryList);
            if (slots.get("Section") != null) {
                queryList.add(new Query(QueryKey.SECTION, slots.get("Section"), QueryOperation.EQUAL, QueryLogic.AND));
            }
            queryList.add(new Query(QueryKey.DEPARTMENT, slots.get("Department"), QueryOperation.EQUAL, QueryLogic.AND));
            queryList.add(new Query(QueryKey.COURSE_NUM, slots.get("CourseNum"), QueryOperation.EQUAL));

            List<Course> resultList = db.read(queryList);
            List<QueryKey> types = Arrays.asList(QueryKey.DAYS);
            response += crb.addSpellOut(slots.get("Department")) + " " + slots.get("CourseNum") +
                    " is taught on " + crb.convertCourse(resultList, types);
        }

        return response;
    }

    private String convertDays(String days) {
        StringBuilder shortened = new StringBuilder();

        for (String s : days.split(" "))
            shortened.append(Character.toUpperCase(s.charAt(0)));

        return shortened.toString();
    }
}
