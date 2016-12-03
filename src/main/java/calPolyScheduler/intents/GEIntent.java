package calPolyScheduler.intents;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import dal.Course;
import dal.Query;
import dal.QueryKey;
import dal.QueryOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class GEIntent extends SchedulerIntent {
    public GEIntent(Intent intent, Session session) {
        super(intent, session);
    }

    @Override
    public SpeechletResponse createResponse() {
        log.info("GE Intent");
        String responseText = determineUtterance();
        return setAnswer(responseText, "GE Intent", responseText);
    }

    private String determineUtterance() {
        CourseResponseBuilder crb = new CourseResponseBuilder();
        List<Query> queryList = new ArrayList<Query>();
        String response = "";

        if (slots.get("CourseRequirement") != null) {
            response = addQuarterYear(queryList);
            queryList.add(new Query(QueryKey.REQUIREMENT, slots.get("CourseRequirement"), QueryOperation.EQUAL));

            List<Course> resultList = db.read(queryList);
            List<QueryKey> types = Arrays.asList(QueryKey.DEPARTMENT, QueryKey.COURSE_NUM);
            response += " Courses that are " + slots.get("CourseRequirement") + " include " + crb.convertCourse(resultList, types);
        }

        return response;
    }
}
