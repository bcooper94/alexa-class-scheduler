package calPolyScheduler.intents;

import calPolyScheduler.Schedule;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import dal.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScheduleManagerIntent extends SchedulerIntent {
    public ScheduleManagerIntent(Intent intent, Session session) {
        super(intent, session);
    }

    public SpeechletResponse createResponse() {
        log.info("Schedule Manager Intent");
        String[] responseText = determineUtterance();
        return setAnswer(responseText[0], "Course Days of Week Intent", responseText[1]);
    }

    private String[] determineUtterance() {
        CourseResponseBuilder crb = new CourseResponseBuilder();
        List<Query> queryList = new ArrayList<Query>();
        String response = "";
        String card = "";
        Schedule schedule = getSchedule();

        if (slots.get("AddRemove")!= null && slots.get("Department") != null &&
                slots.get("CourseNum") != null && slots.get("Section") != null) {
            queryList.add(new Query(QueryKey.SECTION, slots.get("Section"), QueryOperation.EQUAL, QueryLogic.AND));
            queryList.add(new Query(QueryKey.DEPARTMENT, slots.get("Department"), QueryOperation.EQUAL, QueryLogic.AND));
            queryList.add(new Query(QueryKey.COURSE_NUM, slots.get("CourseNum"), QueryOperation.EQUAL));
            List<Course> resultList = db.read(queryList);

            //add
            if (slots.get("AddRemove").equals("add")) {
                response += schedule.addCourse(resultList.get(0)) ?
                        "Successfully added " : "Failed to add ";
            }
            //remove
            else {
                response += schedule.removeCourse(resultList.get(0)) ?
                        "Successfully removed " : "Your schedule did not contain ";
            }

            List<QueryKey> keys = Arrays.asList(QueryKey.DEPARTMENT, QueryKey.COURSE_NUM, QueryKey.SECTION);
            card = new String(response);
            response += crb.convertCourse(resultList, keys);
            card += crb.getCardContent(resultList, keys);
        }
        else {
            //list courses currently stored in schedule
            response += "Your schedule is " + schedule.getList();
            card += "Your schedule is " + schedule.getLisForList();
        }

        return new String[] {response, card};
    }

    private Schedule getSchedule() {
        Schedule s = null;
        if (session.getAttribute("schedule") == null) {
            s = new Schedule();
            session.setAttribute("schedule", s);
        }
        else {
            s = (Schedule) session.getAttribute("schedule");
        }

        return s;
    }
}
