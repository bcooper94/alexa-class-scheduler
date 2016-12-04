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
    private Schedule schedule;

    public ScheduleManagerIntent(Intent intent, Session session, Schedule schedule) {
        super(intent, session);
        this.schedule = schedule;
        log.info("Schedule={}", schedule.getList());
    }

    public SpeechletResponse createResponse() {
        log.info("Schedule Manager Intent");
        String[] responseText = determineUtterance();
        return setAnswer(responseText[0], "Schedule Manager Intent", responseText[1]);
    }

    private String[] determineUtterance() {
        CourseResponseBuilder crb = new CourseResponseBuilder();
        List<Query> queryList = new ArrayList<Query>();
        String response = "";
        String card = "";

        if (slots.get("AddRemove")!= null && slots.get("Department") != null &&
                slots.get("CourseNum") != null && slots.get("Section") != null) {
            queryList.add(new Query(QueryKey.SECTION, slots.get("Section"), QueryOperation.EQUAL, QueryLogic.AND));
            queryList.add(new Query(QueryKey.DEPARTMENT, slots.get("Department"), QueryOperation.EQUAL, QueryLogic.AND));
            queryList.add(new Query(QueryKey.COURSE_NUM, slots.get("CourseNum"), QueryOperation.EQUAL));
            List<Course> resultList = db.read(queryList);

            if (resultList.size() > 0) {
                //add
                if (slots.get("AddRemove").toLowerCase().equals("add")) {
                    response += schedule.addCourse(resultList.get(0)) ?
                            "Successfully added " : "Failed to add ";
                }
                //remove
                else {
                    response += schedule.removeCourse(resultList.get(0)) ?
                            "Successfully removed " : "Your schedule did not contain ";
                }
            }
            else {
                response += "Could not find the requested course ";
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
}
